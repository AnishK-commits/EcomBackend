package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.ResponceDtos.OrderResponseDto;

public interface OrderService {
    OrderResponseDto place_an_order(Long addressId, Users currentUser, PaymentMethod paymentMethod);
}
