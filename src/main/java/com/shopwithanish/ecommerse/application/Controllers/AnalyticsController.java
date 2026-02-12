package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.ResponceDtos.AnalyticsServiceResponceDto;
import com.shopwithanish.ecommerse.application.Services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AnalyticsController {

   @Autowired
    AuthUtil authUtil;
   @Autowired
   AnalyticsService analyticsService;

    @GetMapping("/admin/get-all-details")
    ResponseEntity<?> getAllDetailsAdminDash(){

        Users user= authUtil.LoggedInUser();
        AnalyticsServiceResponceDto dto = analyticsService.getallDetails();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/seller/get-all-details")
    ResponseEntity<?> getAllDetailsSellerDash(){

        Users user= authUtil.LoggedInUser();
        AnalyticsServiceResponceDto dto = analyticsService.getAllDetailsSellerDash();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
