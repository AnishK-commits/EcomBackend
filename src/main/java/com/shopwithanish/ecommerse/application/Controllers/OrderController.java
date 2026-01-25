package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.RequestDtos.OrderRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.OrderResponseDto;
import com.shopwithanish.ecommerse.application.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    AuthUtil authUtil;

    //placing an order
    @PostMapping("/place-an-order/addressId-{addressId}/paymentMethod-{paymentMethod}")
    ResponseEntity<?> place_an_order(@PathVariable Long addressId , @Valid @PathVariable PaymentMethod paymentMethod){


        Users currentUser = authUtil.LoggedInUser();
        OrderResponseDto orderResponseDto= orderService.place_an_order(addressId , currentUser ,paymentMethod );

       return new ResponseEntity<>(orderResponseDto , HttpStatus.OK);
    }
}
