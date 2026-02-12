package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByOrder_OrderId(Long orderId);

    Optional<Payment> findPaymentByRazorpayOrderId(String razorpayOrderId);


}
