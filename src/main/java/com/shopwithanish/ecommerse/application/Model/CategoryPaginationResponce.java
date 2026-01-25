package com.shopwithanish.ecommerse.application.Model;

import com.shopwithanish.ecommerse.application.ResponceDtos.CategoryResponceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPaginationResponce {

    private List<CategoryResponceDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
