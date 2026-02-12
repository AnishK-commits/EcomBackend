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

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Users seller;

    //Now each order item belongs to a seller directly.
    //if not add
//    Seller 1 will see that order
//    Seller 2 will also see that same order
//    So both sellers see full order details p1 of seller 1 p2 of seller 3 becoz they order combine during checkout(including other seller’s product).





}
