package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.ResponceDtos.PaymentResponceDto;

public interface PaymentService {

    PaymentResponceDto intiatePayment(Long orderId);

    void handlePaymentSuccess(String razorpayOrderId, String paymentId);

    void markCashPaymentSuccess(Long orderId);
}
