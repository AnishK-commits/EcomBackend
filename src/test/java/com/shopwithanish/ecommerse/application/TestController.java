package com.shopwithanish.ecommerse.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
