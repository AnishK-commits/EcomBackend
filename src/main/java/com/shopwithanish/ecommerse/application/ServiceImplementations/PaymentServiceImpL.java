package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;

import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Enums.PaymentStatus;
import com.shopwithanish.ecommerse.application.Model.Order;
import com.shopwithanish.ecommerse.application.Model.Payment;
import com.shopwithanish.ecommerse.application.Repository.OrderRepository;
import com.shopwithanish.ecommerse.application.Repository.PaymentRepository;
import com.shopwithanish.ecommerse.application.ResponceDtos.PaymentResponceDto;
import com.shopwithanish.ecommerse.application.Services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpL implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentResponceDto intiatePayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found"));

        paymentRepository.findPaymentByOrder_OrderId(orderId)
                .ifPresent(p -> {
                    throw new ApiException("Payment already initiated");
                });

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setInitiatedAt(LocalDateTime.now());

        if (order.getPaymentMethod() == PaymentMethod.CASH) {
            payment.setPgName("CASH");
            payment.setPaymentStatus(PaymentStatus.PENDING); // waiting for delivery
        } else {
            payment.setPgName("RAZORPAY");
            payment.setPaymentStatus(PaymentStatus.PENDING); // waiting for gateway success
        }

        order.setPayment(payment);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return modelMapper.map(payment, PaymentResponceDto.class);
    }


    @Override
    @Transactional
    public void handlePaymentSuccess(String razorpayOrderId, String paymentId) {

        Payment payment = paymentRepository
                .findPaymentByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new ApiException("Payment record not found"));



        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(paymentId);
        payment.setCompletedAt(LocalDateTime.now());

        Order order = payment.getOrder();
        order.setOrderStatus(OrderStatus.CONFIRMED);

        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    @Transactional
    public void markCashPaymentSuccess(Long orderId) {

        Payment payment = paymentRepository
                .findPaymentByOrder_OrderId(orderId)
                .orElseThrow(() -> new ApiException("Payment not found"));

        if (!"CASH".equals(payment.getPgName())) {
            throw new ApiException("Not a cash payment");
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCompletedAt(LocalDateTime.now());

        Order order = payment.getOrder();
        order.setOrderStatus(OrderStatus.DELIVERED);

        paymentRepository.save(payment);
        orderRepository.save(order);
    }

}
