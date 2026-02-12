package com.shopwithanish.ecommerse.application.RequestDtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDto {
    //we take only product id and n its quantity from frontend rest we calculate here

    private Long productId;
    private Long quantity;
}
