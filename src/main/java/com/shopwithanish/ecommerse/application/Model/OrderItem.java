package com.shopwithanish.ecommerse.application.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    private Order order;

    //one product can coexist as orderitem in diff diff users
    //sarvanchya cart madhe toch similar product item
    @ManyToOne
    private Product product;


    private Long orderItemQuantity;
    private Double orderItemPrice;




}
