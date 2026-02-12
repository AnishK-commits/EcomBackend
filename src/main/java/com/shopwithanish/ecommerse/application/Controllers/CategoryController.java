package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AppConstants;
import com.shopwithanish.ecommerse.application.Model.CategoryPaginationResponce;
import com.shopwithanish.ecommerse.application.RequestDtos.CategoryRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.CategoryResponceDto;
import com.shopwithanish.ecommerse.application.Services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/get-all-categories")
    public ResponseEntity<CategoryPaginationResponce> getAllCategories(@RequestParam(name="pageNumber" , defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                       @RequestParam (name = "pageSize",  defaultValue = AppConstants.CATEGORY_PAGE_SIZE,required = false)Integer pageSize,
                                                                        @RequestParam (name = "sortBy",  defaultValue = AppConstants.SORT_BY ,required = false) String sortBy,
                                                                        @RequestParam (name = "sortOrder",  defaultValue = AppConstants.SORT_DIRECTION ,required = false)String sortOrder){
        CategoryPaginationResponce responce=  categoryService.getAllCategories(pageNumber, pageSize, sortBy , sortOrder);
        return new ResponseEntity<>(responce , HttpStatus.OK);
    }

    @PostMapping("/public/add-new-category")
    public CategoryResponceDto addNewCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto){

        return categoryService.addNewCategory(categoryRequestDto);
    }

    @DeleteMapping("/admin/delete-existing-category/{categoryid}")
    public String deleteCategory(@PathVariable Long categoryid){
       return categoryService.deleteCategory(categoryid);
    }

    @PutMapping("/admin/upadte-existing-category/{categoryid}")
    public CategoryResponceDto updatecategoryobject(@Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                       @PathVariable Long categoryid){
       return categoryService.updatecategoryobject(categoryRequestDto , categoryid);

    }



}
