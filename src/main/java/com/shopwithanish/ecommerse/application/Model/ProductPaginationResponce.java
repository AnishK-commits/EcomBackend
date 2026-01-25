package com.shopwithanish.ecommerse.application.Model;

import com.shopwithanish.ecommerse.application.ResponceDtos.ProductResponceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPaginationResponce {

    private List<ProductResponceDto> content;

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private Boolean lastPage;

}
