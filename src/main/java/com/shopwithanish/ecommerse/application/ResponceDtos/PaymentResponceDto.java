package com.shopwithanish.ecommerse.application.ResponceDtos;


import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponceDto {

    private Long paymentId;

    private Double amount;

    private String pgName; // RAZORPAY

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private LocalDateTime initiatedAt;

    private Long orderId;   // IMPORTANT for frontend flow
}
