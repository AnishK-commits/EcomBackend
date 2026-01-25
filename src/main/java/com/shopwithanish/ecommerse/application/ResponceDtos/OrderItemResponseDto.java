package com.shopwithanish.ecommerse.application.ResponceDtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {


    //private Long productId;
    private Long orderItemId;
    private ProductResponceDto productResponceDto=new ProductResponceDto(); //product id included in this

    private Integer orderItemQuantity;  //actual cart-item quantity
    private Double orderItemPrice;      //actual cart-item price
}
