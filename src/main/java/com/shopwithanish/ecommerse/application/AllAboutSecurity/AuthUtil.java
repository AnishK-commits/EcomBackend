package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String LoggedInEmail() {
         Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
         Users users =userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new ApiException("user not found in authentication by name"));

         return users.getEmail();
    }

    public Users LoggedInUser() {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Users users =userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new ApiException("user not found in authentication "));

        return users;

    }

    public String LoggedInUsername() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Users users =userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new ApiException("user not found in authentication by name"));

        return users.getUsername();
    }

}
