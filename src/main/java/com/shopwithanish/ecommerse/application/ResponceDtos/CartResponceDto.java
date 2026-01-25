package com.shopwithanish.ecommerse.application.ResponceDtos;


import com.shopwithanish.ecommerse.application.Model.CartItem;
import com.shopwithanish.ecommerse.application.Model.Product;
import com.shopwithanish.ecommerse.application.RequestDtos.ProductRequestDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartResponceDto {

    private Long cartId;

    private Double totalCartPrice;

    private List<ProductResponceDto> productResponceDtoList=new ArrayList<>();




}
