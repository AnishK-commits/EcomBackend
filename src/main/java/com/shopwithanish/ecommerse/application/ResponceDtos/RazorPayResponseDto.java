package com.shopwithanish.ecommerse.application.ResponceDtos;

import lombok.Data;

@Data
public class RazorPayResponseDto {

    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
