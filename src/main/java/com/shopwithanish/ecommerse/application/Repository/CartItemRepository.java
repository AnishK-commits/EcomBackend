package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId= ?2 AND ci.product.productId=?1")
    CartItem findCartItemByProductIdAndCartId(Long productid, Long cartId);

    //when delete something we need to add modify while selecting no need to add , also while updating need to add
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2")
    void deleteCartItemByCartIdAndByProductId(Long cartId, Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1")
    void deleteAllCartItemByCartId(Long cartId);
}