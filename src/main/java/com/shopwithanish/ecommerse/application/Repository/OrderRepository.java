package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ Use JOIN FETCH to eagerly load order items
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItemList WHERE o.email = :email ORDER BY o.orderDate DESC")
    List<Order> findByEmailOrderByOrderDateDesc(@Param("email") String email);

    // ✅ Calculate total revenue
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double calculateTotalRevenue();

    // ✅ Revenue by order status (e.g., only DELIVERED orders)
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderStatus = :orderStatus")
    Double calculateRevenueByStatus(@Param("orderStatus") OrderStatus orderStatus);

    // ✅ Revenue for a specific date range
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Double calculateRevenueBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // ✅ Revenue for current month
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Double calculateCurrentMonthRevenue();

    // ✅ Revenue for current year
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Double calculateCurrentYearRevenue();




    @Query("""
            SELECT DISTINCT o
                                            FROM Order o
                                            JOIN o.orderItemList oi
                                            WHERE oi.seller.userid = :sellerUserId
            
       """)
    Page<Order> findOrdersBySeller(Long sellerUserId, Pageable pageable);

    @Query("""
       SELECT COUNT(DISTINCT o)
       FROM Order o
       JOIN o.orderItemList oi
       WHERE oi.seller.userid = :sellerId
       """)
    Long countOrdersBySeller(@Param("sellerId") Long sellerId);


    @Query("""
       SELECT SUM(oi.orderItemPrice * oi.orderItemQuantity)
       FROM OrderItem oi
       WHERE oi.seller.userid = :sellerId
       """)
    Double calculateTotalRevenueBySeller(@Param("sellerId") Long sellerId);

}