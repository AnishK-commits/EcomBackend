package com.shopwithanish.ecommerse.application.RequestDtos;

import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Long addressId;
    private PaymentMethod paymentMethod;
}
