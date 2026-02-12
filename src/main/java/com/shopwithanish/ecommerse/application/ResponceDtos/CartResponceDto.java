package com.shopwithanish.ecommerse.application.ResponceDtos;


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
