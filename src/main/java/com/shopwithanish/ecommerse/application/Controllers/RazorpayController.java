package com.shopwithanish.ecommerse.application.Controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.AllAboutSecurity.RazorpaySignatureUtil;
import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Payment;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.PaymentRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.RazorPayOrderRequest;
import com.shopwithanish.ecommerse.application.ResponceDtos.RazorPayResponseDto;
import com.shopwithanish.ecommerse.application.Services.PaymentService;
import com.shopwithanish.ecommerse.application.Services.RazorpayCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RazorpayController {

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.key.id}")
    private String keyId;

    private final RazorpaySignatureUtil signatureUtil;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final RazorpayCustomerService razorpayCustomerService;
    private final AuthUtil authUtil;

    @PostMapping("/create-order-razorpay-payment-reference")
    public ResponseEntity<?> createOrder(@RequestBody RazorPayOrderRequest req) throws Exception {

        // Fetch payment from database
        Payment payment = paymentRepository
                .findPaymentByOrder_OrderId(req.getOrderId())
                .orElseThrow(() -> new ApiException("Payment not found"));

        Double amount = payment.getAmount();

        if (amount == null || amount <= 0) {
            throw new ApiException("Payment amount in database is null or zero.");
        }

        // Convert to paise
        int amountInPaise = (int) Math.round(amount * 100);

        log.info("Order ID: {} | Amount: ₹{} | Razorpay Amount: {} paise",
                req.getOrderId(), amount, amountInPaise);

        // ✅ Get current user and ensure Razorpay customer exists
        Users currentUser = authUtil.LoggedInUser();
        String razorpayCustomerId = razorpayCustomerService.createOrFetchCustomer(currentUser);

        log.info("Using Razorpay customer ID: {} for user: {}",
                razorpayCustomerId, currentUser.getEmail());

        // Create Razorpay client
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        // Create order options
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcpt_" + req.getOrderId());
        orderRequest.put("customer_id", razorpayCustomerId); // ✅ Link customer

        // Optional: Add notes
        JSONObject notes = new JSONObject();
        notes.put("order_id", req.getOrderId());
        notes.put("user_id", currentUser.getUserid());
        notes.put("email", currentUser.getEmail());
        notes.put("contact",currentUser.getPhoneNo());
        orderRequest.put("notes", notes);

        // Create Razorpay order
        Order razorpayOrder = client.orders.create(orderRequest);

        log.info("Razorpay order created: {}", (Object) razorpayOrder.get("id"));

        // Save Razorpay order ID to payment
        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        paymentRepository.save(payment);

        // Prepare response
        Map<String, Object> response = razorpayOrder.toJson().toMap();

        // ✅ Add user details for prefill in frontend
        response.put("razorpayOrderId", razorpayOrder.get("id")); // 🔑 important
        response.put("amount", razorpayOrder.get("amount"));
        response.put("currency", razorpayOrder.get("currency"));
        response.put("receipt", razorpayOrder.get("receipt"));

        response.put("razorpayKeyId", keyId);
        response.put("userName", currentUser.getUsername());
        response.put("email", currentUser.getEmail());
        response.put("contact", currentUser.getPhoneNo());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody RazorPayResponseDto res) {

        String data = res.getRazorpayOrderId() + "|" + res.getRazorpayPaymentId();

        String generatedSignature = signatureUtil.generateSignature(data, keySecret);

        if (!generatedSignature.equals(res.getRazorpaySignature())) {
            log.error("Payment signature verification failed");
            return ResponseEntity.status(400).body("Invalid Payment Signature");
        }

        log.info("Payment verified successfully. Payment ID: {}", res.getRazorpayPaymentId());

        // Mark payment as success
        paymentService.handlePaymentSuccess(
                res.getRazorpayOrderId(),
                res.getRazorpayPaymentId()
        );

        return ResponseEntity.ok("Payment Verified & Order Confirmed");
    }
}