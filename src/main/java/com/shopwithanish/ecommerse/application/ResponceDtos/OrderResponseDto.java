package com.shopwithanish.ecommerse.application.ResponceDtos;

import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Double totalAmount;
    private PaymentMethod paymentMethod;
    private List<OrderItemResponseDto> orderItemResponseDtoList=new ArrayList<>();

    //private PaymentResponceDto paymentResponceDto;
    private Long addressId;
}
