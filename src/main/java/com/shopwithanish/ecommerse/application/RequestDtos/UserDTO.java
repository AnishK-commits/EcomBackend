package com.shopwithanish.ecommerse.application.RequestDtos;

import com.shopwithanish.ecommerse.application.Model.Addresss;
import com.shopwithanish.ecommerse.application.Model.Cart;
import com.shopwithanish.ecommerse.application.Model.Order;
import com.shopwithanish.ecommerse.application.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userid;
    private String username;
    private String password;
    private String email;
    private String phoneNo;

    Set<Role> roles= new HashSet<>();

    private Cart cart;
    private List<Order> orders=new ArrayList<>();

    private List<Addresss> addresssList=new ArrayList<>();

}
