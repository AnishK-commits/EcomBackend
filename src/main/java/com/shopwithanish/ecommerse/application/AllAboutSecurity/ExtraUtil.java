package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.DataSource;

@Configuration
public class ExtraUtil {

//    @Autowired
//    DataSource dataSource;
//
//    @Autowired
//    UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    //why this bean
    //in Auth greetings controller we are injecting AuthenticationManager obj
    //but to inject we need to crate that object that what we are doing here @Bean create object and return object
    //and that object we use then same with password encoder

    // 2. Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    // 3. In-memory user: anish / anish123
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//
//        UserDetails user1 = User.builder()
//                .username("mahesh")
//                .password(encoder.encode("mahesh123"))
//                .roles("USER")
//                .build();
//        UserDetails user2 = User.builder()
//                .username("raj")
//                .password(encoder.encode("raj123"))
//                .roles("USER")
//                .build();
//        UserDetails user3 = User.builder()
//                .username("dipak")
//                .password(encoder.encode("dipak123"))
//                .roles("USER")
//                .build();
//        UserDetails user4 = User.builder()
//                .username("anish")
//                .password(encoder.encode("anish123"))
//                .roles("ADMIN")
//                .build();
//
//        JdbcUserDetailsManager jdbcUserDetailsManager=new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.createUser(user1);
//        jdbcUserDetailsManager.createUser(user2);
//        jdbcUserDetailsManager.createUser(user3);
//        jdbcUserDetailsManager.createUser(user4);
//
//
//        return jdbcUserDetailsManager;
//    }




}
