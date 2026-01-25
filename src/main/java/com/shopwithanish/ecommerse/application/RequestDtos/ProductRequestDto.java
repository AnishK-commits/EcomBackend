package com.shopwithanish.ecommerse.application.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {


    private String productName;
    private String description;
    private Long stock;
    private Double price;
    private Double discount;
    private String image;
}
