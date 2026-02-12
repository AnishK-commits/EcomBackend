package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Model.OrdersPaginationResponce;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.ResponceDtos.OrderResponseDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {
    OrderResponseDto place_an_order(Long addressId, Users currentUser, PaymentMethod paymentMethod);



    // ✅ Add method to fetch user orders with items
    @Transactional
    List<OrderResponseDto> getOrdersForLoggedInUser(String email);

    OrdersPaginationResponce getOrdersInWholeDataBase(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderResponseDto getOrderDetailFromOrderId(Long orderId);

    OrderResponseDto updateOrderStatus(Long orderId , OrderStatus status);

    OrdersPaginationResponce getAllOrdersofSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderResponseDto getOrderDetailFromOrderIdlSELLERView(Long orderId);
}
