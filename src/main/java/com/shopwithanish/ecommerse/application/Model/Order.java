package com.shopwithanish.ecommerse.application.Model;



import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"orderItemList", "user", "addresss", "payment"})
@EqualsAndHashCode(exclude = {"orderItemList", "user", "addresss", "payment"})
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    List<OrderItem> orderItemList=new ArrayList<>();

    private LocalDateTime orderDate;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;



    //multiple order will ship to single address in future
    @ManyToOne
    private Addresss addresss;//

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;



    @ManyToOne
    private Users user;



}
