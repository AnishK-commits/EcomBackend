package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.ResponceDtos.PaymentResponceDto;

public interface PaymentService {

    PaymentResponceDto intiatePayment(Long orderid);
}
