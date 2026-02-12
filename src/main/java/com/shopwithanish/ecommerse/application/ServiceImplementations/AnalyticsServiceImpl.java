package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.OrderRepository;
import com.shopwithanish.ecommerse.application.Repository.ProductRepository;
import com.shopwithanish.ecommerse.application.ResponceDtos.AnalyticsServiceResponceDto;
import com.shopwithanish.ecommerse.application.Services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthUtil authUtil;

    @Override
    public AnalyticsServiceResponceDto getallDetails() {

        productRepository.count();
        orderRepository.count();
        // ✅ Calculate different revenue metrics
        Double totalRevenue = orderRepository.calculateTotalRevenue();
        Double deliveredRevenue = orderRepository.calculateRevenueByStatus(OrderStatus.DELIVERED);
        Double monthlyRevenue = orderRepository.calculateCurrentMonthRevenue();
        Double yearlyRevenue = orderRepository.calculateCurrentYearRevenue();

        // Handle null cases (when no orders exist)
        totalRevenue = (totalRevenue != null) ? totalRevenue : 0.0;
        deliveredRevenue = (deliveredRevenue != null) ? deliveredRevenue : 0.0;
        monthlyRevenue = (monthlyRevenue != null) ? monthlyRevenue : 0.0;
        yearlyRevenue = (yearlyRevenue != null) ? yearlyRevenue : 0.0;

        AnalyticsServiceResponceDto dto = new AnalyticsServiceResponceDto();
        dto.setProductCount(productRepository.count());
        dto.setTotalOrders(orderRepository.count());
        dto.setTotalRevenue(totalRevenue);

        return dto;
    }

    @Override
    public AnalyticsServiceResponceDto getAllDetailsSellerDash() {

        Users seller = authUtil.LoggedInUser();
        Long sellerId = seller.getUserid();

        Long productCount = productRepository.countBySeller_Userid(sellerId);

        Long totalOrders = orderRepository.countOrdersBySeller(sellerId);

        Double totalRevenue =
                orderRepository.calculateTotalRevenueBySeller(sellerId);

//        Double deliveredRevenue =
//                orderRepository.calculateRevenueBySellerAndStatus(
//                        sellerId, OrderStatus.DELIVERED);
//
//        Double monthlyRevenue =
//                orderRepository.calculateCurrentMonthRevenueBySeller(sellerId);
//
//        Double yearlyRevenue =
//                orderRepository.calculateCurrentYearRevenueBySeller(sellerId);

        // Handle nulls
        totalRevenue = totalRevenue != null ? totalRevenue : 0.0;
//        deliveredRevenue = deliveredRevenue != null ? deliveredRevenue : 0.0;
//        monthlyRevenue = monthlyRevenue != null ? monthlyRevenue : 0.0;
//        yearlyRevenue = yearlyRevenue != null ? yearlyRevenue : 0.0;

        AnalyticsServiceResponceDto dto = new AnalyticsServiceResponceDto();
        dto.setProductCount(productCount);
        dto.setTotalOrders(totalOrders);
        dto.setTotalRevenue(totalRevenue);
//        dto.setDeliveredRevenue(deliveredRevenue);
//        dto.setMonthlyRevenue(monthlyRevenue);
//        dto.setYearlyRevenue(yearlyRevenue);

        return dto;
    }

}
