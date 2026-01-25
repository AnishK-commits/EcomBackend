package com.shopwithanish.ecommerse.application.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartitemId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_cartid")
    private Cart cart;   //fkwillbe here and join column lihala nahi tari bante mapping


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_productid" )
    private Product product;
    // One Product can appear in many CartItems (because many users can add the same product to their carts).
    //Each CartItem always points to one specific Product.

    private Long quantity;
    private Double productsPrice;
    private Double discountOnProduct;


}