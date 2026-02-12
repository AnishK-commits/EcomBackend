package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {


    // ✅ USE JOIN FETCH to eagerly load cart items
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItemList WHERE c.users.email = :email")
    Optional<Cart> findCartByEmail(@Param("email") String email);

    // ✅ Also fix this one
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItemList WHERE c.users.email = :email AND c.cartId = :cartId")
    Cart findCartByEmailAndByCartId(@Param("email") String email, @Param("cartId") Long cartId);


    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.cartItemList cil " +
            "JOIN FETCH cil.product p " +
            "WHERE p.productId = ?1")
    List<Cart> findCartsByProductId(Long productId);

}