package com.shopwithanish.ecommerse.application.AllCustomExceptions;

public class ApiException extends RuntimeException {

    public ApiException(String s) {
        super(s);
    }
}
