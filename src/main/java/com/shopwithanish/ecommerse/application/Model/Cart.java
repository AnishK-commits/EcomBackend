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
@ToString(exclude = {"cartItemList", "users"}) // ✅ CRITICAL FIX
@EqualsAndHashCode(exclude = {"cartItemList", "users"})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @JsonIgnore
    @OneToMany(mappedBy = "cart" , cascade = CascadeType.ALL ,orphanRemoval = true , fetch = FetchType.EAGER)
    private List<CartItem> cartItemList=new ArrayList<>();
    //parent is cart

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "fk_userid")
    private Users users;
    //can set anyone with fk

    private Double totalCartPrice;
}
