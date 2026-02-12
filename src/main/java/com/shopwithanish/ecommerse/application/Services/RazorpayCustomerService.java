package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Model.Users;

public interface RazorpayCustomerService {
    String createOrFetchCustomer(Users user);
    String createCustomer(Users user);
}