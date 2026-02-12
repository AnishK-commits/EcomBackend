package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.RequestDtos.AddressRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.AddressResponceDto;
import com.shopwithanish.ecommerse.application.Services.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressService addressService;
    @Autowired
    AuthUtil authUtil;

    //add a address to user
    @PostMapping ("/add-a-address-to-user")
    ResponseEntity<?> add_a_address_to_user(@Valid @RequestBody AddressRequestDto addressRequestDto){

        Users user= authUtil.LoggedInUser();
           AddressResponceDto dto= addressService.add_a_address_to_user(addressRequestDto, user );
               return new ResponseEntity<>(dto , HttpStatus.OK);
    }

    //get all a address  r
    @GetMapping ("/get-all-address")
    ResponseEntity<?> getall(){

        Users user= authUtil.LoggedInUser();
        List<AddressResponceDto > dtoList = addressService.getall();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }


    //get address by address id
    @GetMapping ("/get-address-by-addressid/{addressid}")
    ResponseEntity<?> getaddressbyaddressidl(@PathVariable Long addressid){

        Users user= authUtil.LoggedInUser();
        AddressResponceDto  dtoList = addressService.getaddressbyaddressidl(addressid);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping ("/get-address-by-user")
    ResponseEntity<?> getaddressbyloggeduser(){

        Users user= authUtil.LoggedInUser();
        List<AddressResponceDto>  dtoList = addressService.getaddressbyloggeduser(user);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }


    @PutMapping ("/update-address-of-user/{adderessid}")
    ResponseEntity<?> updateaddressofuserbyAddesssid( @PathVariable Long adderessid , @Valid @RequestBody AddressRequestDto addressRequestDto){

        AddressResponceDto  dtoList = addressService.updateaddressofuserbyAddesssid(adderessid , addressRequestDto);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }


    @DeleteMapping ("/delete-address-of-user/{adderessid}")
    ResponseEntity<?> deleteaddressofuserbyAddesssid( @PathVariable Long adderessid ){

        String  msg = addressService.deleteaddressofuserbyAddesssid(adderessid );
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }


}
