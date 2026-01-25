package com.shopwithanish.ecommerse.application.ResponceDtos;


import com.shopwithanish.ecommerse.application.Model.Product;
import com.shopwithanish.ecommerse.application.RequestDtos.ProductRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponceDto {

    private Long cartitemId;
    private CartResponceDto cartRequestDto;
    private Product product;

    private Long quantity;
    private Long price;
    private Double discount;


}
