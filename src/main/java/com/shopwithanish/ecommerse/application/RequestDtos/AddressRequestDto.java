package com.shopwithanish.ecommerse.application.RequestDtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {

    @NotBlank
    private String buildingname;

    @NotBlank
    private String street;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @Min(100000)
    @Max(999999)
    private Integer pincode;
}
