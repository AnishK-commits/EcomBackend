package com.shopwithanish.ecommerse.application.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String description;
    private Long   stock;
    private Double price;
    private Double specialPrice;
    private String image;
    private Double discount;
    private Long inCartQuantity= 0L;

    @ManyToOne
    @JsonIgnore
    private Category category;


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Users seller;              //product contain user and user is a seller type

    @JsonIgnore
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
    private List<CartItem> cartItemList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList=new ArrayList<>();

}
