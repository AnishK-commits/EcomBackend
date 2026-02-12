package com.shopwithanish.ecommerse.application.ResponceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsServiceResponceDto {

    private Long productCount;
    private Long totalOrders;
    private Double totalRevenue;
}
