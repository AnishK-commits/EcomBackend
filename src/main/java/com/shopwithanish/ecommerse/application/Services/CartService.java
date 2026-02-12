package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Model.Cart;
import com.shopwithanish.ecommerse.application.Model.CartItem;
import com.shopwithanish.ecommerse.application.RequestDtos.CartItemRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.CartResponceDto;

import java.util.List;

public interface CartService {

    CartResponceDto addProductToCartFN(Long productid, Long quantity);

    List<CartResponceDto> fetchAllcarts();

    CartResponceDto fetchLoggedUserCartDetail(String email, Long cartId);

    CartResponceDto updateQuantityOfProductinCart(Long productId, Long opera);

    String deleteProductFromCart(Long cartId, Long productId);

    CartItem updateThisCartItemFN(Cart cart , CartItem cartItem, Long productid);

    String createOrUpdateCartOneLastTime(List<CartItemRequestDto> cartitemslist);
}
