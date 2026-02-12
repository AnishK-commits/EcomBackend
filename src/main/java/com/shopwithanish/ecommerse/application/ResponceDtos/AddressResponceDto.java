package com.shopwithanish.ecommerse.application.ResponceDtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponceDto {

    private Long addressid;

    private String buildingname;


    private String street;

    private String country;


    private String city;
    private String state;



    private Integer pincode;

    private String phoneNo;

}
