package com.shopwithanish.ecommerse.application.RequestDtos;

import lombok.Data;

@Data
public class RazorPayOrderRequest {

    private Long orderId;
    private Double amount;   // you said you want to send this intentionally
}
