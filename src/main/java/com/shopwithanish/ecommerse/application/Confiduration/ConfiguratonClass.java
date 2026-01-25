package com.shopwithanish.ecommerse.application.Confiduration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguratonClass {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
