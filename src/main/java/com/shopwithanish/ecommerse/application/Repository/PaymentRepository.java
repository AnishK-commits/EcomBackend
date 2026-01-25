package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment , Long> {


    @Query("SELECT p FROM Payment p WHERE p.order.orderId = ?1")
    Optional<Payment> findPaymentByOrderId(Long orderId);

}
