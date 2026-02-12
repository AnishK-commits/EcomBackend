package com.shopwithanish.ecommerse.application.RequestDtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Size(min = 5)
    private String username;

    @NotBlank
    @Size(min = 5)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNo;


    private Set<String> roles=new HashSet<>();
    //user role as normal string pass karel tyala kuth aplaya Role class baddal mahit ahe

}
