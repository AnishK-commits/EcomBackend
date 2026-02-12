package com.shopwithanish.ecommerse.application.ResponceDtos;


import com.shopwithanish.ecommerse.application.RequestDtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private List<UserDTO> content = new ArrayList<>();
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}

//convert each entry of user in repo to userDTO then convert all userDTO to UserResponceDto