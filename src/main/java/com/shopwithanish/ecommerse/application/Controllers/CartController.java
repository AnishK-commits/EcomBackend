package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Cart;
import com.shopwithanish.ecommerse.application.Repository.CartItemRepository;
import com.shopwithanish.ecommerse.application.Repository.CartRepository;
import com.shopwithanish.ecommerse.application.ResponceDtos.CartResponceDto;
import com.shopwithanish.ecommerse.application.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;
    @Autowired
    AuthUtil authUtil;
    @Autowired
    CartRepository cartRepository;

    @PostMapping("/add-product-to-cart/{productid}/{requestedQuantity}")
    public ResponseEntity<?> addProductToCartFN(@PathVariable Long productid,
                                                              @PathVariable Long requestedQuantity)
    {

               CartResponceDto cart =cartService.addProductToCartFN( productid, requestedQuantity);
               return new ResponseEntity<>(cart , HttpStatus.OK);
    }

    //for promotional campaign
    @GetMapping("/fetch-all-carts-of-all-user")
    public ResponseEntity<?> fetchAllcarts(){

        //single user > one cart> one cart respnce dto
        //multiple users> many catrs> many/List of cartResponceDto
        List<CartResponceDto>   cartResponceDtoList=cartService.fetchAllcarts();
        return new ResponseEntity<>(cartResponceDtoList , HttpStatus.OK);
    }

    //fetch logged user cart details
    @GetMapping("/fetch-logged-user-cart-details")
    public ResponseEntity<?>  fetchLoggedUserCartDetail(){
        String email= authUtil.LoggedInEmail();
          Cart cart=    cartRepository.findCartByEmail(email).orElseThrow(()->new ApiException("No Cart associated with this email"));
          Long cartId= cart.getCartId();
        CartResponceDto cartResponceDto =  cartService.fetchLoggedUserCartDetail(email , cartId);
        return new ResponseEntity<>(cartResponceDto , HttpStatus.OK);
    }

    //update quantity of product in cart
    @GetMapping("/update-quantity-of-product-in-cart/{productId}/{operation}")
    public ResponseEntity<?> updateQuantityOfProductinCart(@PathVariable Long productId , @PathVariable String operation){

        Long opera= (long) (operation.equalsIgnoreCase("minus")? -1 : 1);
       CartResponceDto cartResponceDto=  cartService.updateQuantityOfProductinCart(productId , opera);
        return ResponseEntity.ok(cartResponceDto);
    }

    //delete product from cart
    @DeleteMapping("/delete-product-from-cart/cartId-{cartId}/productId-{productId}")
    public ResponseEntity<?> deleteProductFromCart( @PathVariable Long cartId , @PathVariable Long productId){

       java.lang.String str  =cartService.deleteProductFromCart(cartId , productId);
       return new ResponseEntity<>(str , HttpStatus.OK);
    }

}
