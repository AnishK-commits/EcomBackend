package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.RequestDtos.AddressRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.AddressResponceDto;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {

    AddressResponceDto add_a_address_to_user(@Valid AddressRequestDto addressRequestDto, Users user);

    List<AddressResponceDto> getall();

    AddressResponceDto getaddressbyaddressidl(Long addressid);

    List<AddressResponceDto> getaddressbyloggeduser(Users user);

    AddressResponceDto updateaddressofuserbyAddesssid(Long adderessid, AddressRequestDto addressRequestDto);

    String deleteaddressofuserbyAddesssid(Long adderessid);
}
