package com.shopwithanish.ecommerse.application.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Addresss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long addressid;

    @NotBlank
    private String buildingname;

    @NotBlank
    private String street;

    @NotBlank
    private String country;

    @NotBlank
    private String city;


// @size does not work on integer it works only with string
//    @Size(max = 6 , message = "it should be minimum 6 chars")
    private Integer pincode;

    @ManyToOne
    private Users user;

    @OneToMany(mappedBy ="addresss" )
    private List<Order> orderList=new ArrayList<>();

}
