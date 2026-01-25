package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Addresss;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.AddressRepository;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.AddressRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.AddressResponceDto;
import com.shopwithanish.ecommerse.application.Services.AddressService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public AddressResponceDto add_a_address_to_user(AddressRequestDto addressRequestDto, Users user) {

        Addresss addresss = modelMapper.map(addressRequestDto, Addresss.class);

        // ✅ establish relationship FIRST
        addresss.setUser(user);
        user.getAddresssList().add(addresss);

        // ✅ now save
        Addresss saved = addressRepository.save(addresss);

        return modelMapper.map(saved, AddressResponceDto.class);
    }

    @Override
    public List<AddressResponceDto> getall() {

      List<Addresss> addresssList=  addressRepository.findAll();
     return addresssList.stream().map(addresss -> modelMapper.map(addresss ,AddressResponceDto.class)).collect(Collectors.toList());

    }

    @Override
    public AddressResponceDto getaddressbyaddressidl(Long addressid) {

       Addresss addresss= addressRepository.findById(addressid).orElseThrow(()-> new ApiException("no address found for this id :"+ addressid));
        return modelMapper.map(addresss, AddressResponceDto.class);
    }

    @Override
    public List<AddressResponceDto> getaddressbyloggeduser(Users user) {


        List<Addresss> addresssList=  user.getAddresssList();
        return addresssList.stream().map(addresss -> modelMapper.map(addresss ,AddressResponceDto.class)).collect(Collectors.toList());

    }

    @Override
    public AddressResponceDto updateaddressofuserbyAddesssid(Long adderessid, AddressRequestDto addressRequestDto) {

       Addresss addresss= addressRepository.findById(adderessid).orElseThrow(()-> new ApiException("no addresss exist link to this id: "+adderessid));

       addresss.setCity(addressRequestDto.getCity());
       addresss.setPincode(addressRequestDto.getPincode());
       addresss.setCountry(addressRequestDto.getCountry());
       addresss.setBuildingname(addresss.getBuildingname());
       addresss.setStreet(addressRequestDto.getStreet());
     Addresss updatedone= addressRepository.save(addresss);

      //after this we need to remove old address that is present in user addresslist
        Users user = addresss.getUser(); //link user

        user.getAddresssList().removeIf(addresss1 -> addresss1.getAddressid().equals(adderessid));
         //address 1 is old addressin list

        user.getAddresssList().add(updatedone);
        userRepository.save(user);
        return modelMapper.map(addresss , AddressResponceDto.class);
    }

    @Override
    public String deleteaddressofuserbyAddesssid(Long adderessid) {

        Addresss addresssinDB= addressRepository.findById(adderessid).orElseThrow(()->  new ApiException("no address found link with this id"));

        Users user = addresssinDB.getUser();
        user.getAddresssList().removeIf(addresss -> addresss.getAddressid().equals(adderessid));
        userRepository.save(user);

        addressRepository.delete(addresssinDB);


        return "DELETED !!!";
    }

}