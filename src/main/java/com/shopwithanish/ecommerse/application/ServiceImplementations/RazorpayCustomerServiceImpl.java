package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.razorpay.Customer;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;
import com.shopwithanish.ecommerse.application.Services.RazorpayCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RazorpayCustomerServiceImpl implements RazorpayCustomerService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final UserRepository userRepository;

    @Override
    public String createOrFetchCustomer(Users user) {
        // If customer already has Razorpay ID, return it
        if (user.getRazorpayCustomerId() != null && !user.getRazorpayCustomerId().isEmpty()) {
            log.info("Razorpay customer already exists for user: {}", user.getEmail());
            return user.getRazorpayCustomerId();
        }

        // Otherwise create new customer
        return createCustomer(user);
    }

    @Override
    public String createCustomer(Users user) {
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject customerRequest = new JSONObject();
            customerRequest.put("name", user.getUsername());
            customerRequest.put("email", user.getEmail());

            // Optional: Add phone if available
            // customerRequest.put("contact", user.getPhone());

            // Optional: Add notes
            JSONObject notes = new JSONObject();
            notes.put("user_id", user.getUserid());
            customerRequest.put("notes", notes);

            Customer customer = client.customers.create(customerRequest);
            String customerId = customer.get("id");

            log.info("Created Razorpay customer: {} for user: {}", customerId, user.getEmail());

            // Save customer ID to database
            user.setRazorpayCustomerId(customerId);
            userRepository.save(user);

            return customerId;

        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay customer for user: {}", user.getEmail(), e);
            throw new ApiException("Failed to create payment customer: " + e.getMessage());
        }
    }
}