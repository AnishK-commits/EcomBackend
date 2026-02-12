package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.ResponceDtos.AddressResponceDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.AnalyticsServiceResponceDto;

import java.util.List;

public interface AnalyticsService {

    AnalyticsServiceResponceDto getallDetails();

    AnalyticsServiceResponceDto getAllDetailsSellerDash();
}
