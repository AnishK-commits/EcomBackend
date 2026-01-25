package com.shopwithanish.ecommerse.application.ResponceDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductResponceDto {

    private Long productId;
    private String productName;
    private String description;


    private Long quantity=0L;

    private Double price;
    private Double specialPrice;
    private Double discount;
    private String image;
    private String stock;
}
