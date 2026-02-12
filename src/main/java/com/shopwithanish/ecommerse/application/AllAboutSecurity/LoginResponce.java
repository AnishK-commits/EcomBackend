package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponce {

    private String JWTtoken;
    private String username;

  private List<String> roles;
    private Long userid;
}
