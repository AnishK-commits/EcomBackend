package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.AppConstants;
import com.shopwithanish.ecommerse.application.RequestDtos.SignupRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.UserResponseDTO;
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


    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserResponseDTO> getAllUsers(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                         @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                         @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYUSERS, required = false) String sortBy,
                                                         @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {


        UserResponseDTO dto=authService.getAllUsers(pageNumber, pageSize , sortBy ,sortOrder);

        return new ResponseEntity<>(dto , HttpStatus.OK);
    }

    //UserResponseDTO includes all list of registered users and pagination response

    @GetMapping("/admin/get-all-sellers")
    public ResponseEntity<UserResponseDTO> getAllSellers(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                       @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                       @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYUSERS, required = false) String sortBy,
                                                       @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {


        UserResponseDTO dto=authService.getAllSellers(pageNumber, pageSize , sortBy ,sortOrder);

        return new ResponseEntity<>(dto , HttpStatus.OK);
    }

}
