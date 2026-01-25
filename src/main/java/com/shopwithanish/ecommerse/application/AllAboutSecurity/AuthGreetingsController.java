package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.RequestDtos.SignupRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthGreetingsController {

    @Autowired
    AuthService authService;


    @GetMapping("/hello")
    public ResponseEntity<?> hellofn(){
        return new ResponseEntity<>("hello brother", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginInUserfn(@Valid @RequestBody  LoginRequest loginRequest){

              return authService.LoginInUserfn(loginRequest);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpNewUser(@Valid @RequestBody SignupRequestDto signupRequestDto){
        return authService.signUpNewUser(signupRequestDto);
    }

    @GetMapping("/uname")
    public ResponseEntity<?> getCurrentNname(Authentication authentication){
        //here spring fetches authentication obj automatically when user login ND authentication is successful
        return authService.getCurrentNname(authentication);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUserFn(){
        return authService.signOutUserFn();
    }



}
