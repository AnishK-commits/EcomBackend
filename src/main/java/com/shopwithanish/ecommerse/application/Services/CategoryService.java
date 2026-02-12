package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Model.CategoryPaginationResponce;
import com.shopwithanish.ecommerse.application.RequestDtos.CategoryRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.CategoryResponceDto;

public interface CategoryService {

   CategoryPaginationResponce getAllCategories(Integer pageNumber, Integer pageSize,  String sortBy,String sortdirection );

    CategoryResponceDto addNewCategory(CategoryRequestDto categoryRequestDto);

    String deleteCategory(Long categoryid);

    CategoryResponceDto updatecategoryobject(CategoryRequestDto categoryRequestDto, Long categoryid);
}
