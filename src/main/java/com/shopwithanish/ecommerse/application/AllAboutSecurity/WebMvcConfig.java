package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//to tell backend if request comes form frontend to see imagae then look image in this folder
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**").addResourceLocations("file:imagesfolder/");
                                         //if this present in frontend url
                                        //then go to this folder
    }
}
