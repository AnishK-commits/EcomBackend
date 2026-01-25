package com.shopwithanish.ecommerse.application.Model;


import com.shopwithanish.ecommerse.application.Enums.AppPaymentStatus;
import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    List<OrderItem> orderItemList=new ArrayList<>();

    private LocalDateTime orderDate;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;


    @Enumerated(value = EnumType.STRING)
    private AppPaymentStatus appPaymentStatus;

    //multiple order will ship to single address in future
    @ManyToOne
    private Addresss addresss;//

    @OneToOne
    private Payment payment;


    @ManyToOne
    private Users user;


}
