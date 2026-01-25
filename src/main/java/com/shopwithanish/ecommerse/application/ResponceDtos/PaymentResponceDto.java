package com.shopwithanish.ecommerse.application.ResponceDtos;

import com.shopwithanish.ecommerse.application.Enums.AppPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter@AllArgsConstructor@NoArgsConstructor
public class PaymentResponceDto {

    private Long paymentId;
    private Double amount;
    private AppPaymentStatus paymentStatus;

    // later:
    // private String paymentUrl;
}
