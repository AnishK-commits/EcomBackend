package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {


    @Query("SELECT c FROM Cart c WHERE c.users.email = ?1")
    Optional<Cart> findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.users.email = ?1 AND c.cartId=?2")
    Cart findCartByEmailAndByCartId(String email, Long cartId);


    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.cartItemList cil " +
            "JOIN FETCH cil.product p " +
            "WHERE p.productId = ?1")
    List<Cart> findCartsByProductId(Long productId);

}