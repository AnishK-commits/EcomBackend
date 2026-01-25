package com.shopwithanish.ecommerse.application.AllCustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public class ApiError {

    private String msg;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;

    public ApiError(String msg , HttpStatus httpStatus){
        this.msg=msg;
        this.httpStatus=httpStatus;
        this.timestamp=LocalDateTime.now();
    }

    public HttpStatusCode getHttpStatus() {
        return this.httpStatus;
    }

    public String  getMessage() {
       return this.msg;
    }
}
