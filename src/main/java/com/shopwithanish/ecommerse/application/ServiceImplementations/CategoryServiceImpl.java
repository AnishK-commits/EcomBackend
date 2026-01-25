package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Category;
import com.shopwithanish.ecommerse.application.Model.CategoryPaginationResponce;
import com.shopwithanish.ecommerse.application.Repository.CategoryRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.CategoryRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.CategoryResponceDto;
import com.shopwithanish.ecommerse.application.Services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

//    @Override
//    public List<CategoryResponceDto> getAllCategories() { //without pagination
//        List<Category> categoryList=categoryRepository.findAll();
//        if(categoryList.isEmpty()){
//            throw new ApiException("No category added till yet. list is empty");
//        }
//        categoryList= categoryRepository.findAll(); //currently empty list will return
//        List<CategoryResponceDto> categoryResponceDtoList=new ArrayList<>();
//        for(Category cc:categoryList){
//           CategoryResponceDto categoryResponceDto =  modelMapper.map(cc ,CategoryResponceDto.class);
//            categoryResponceDtoList.add(categoryResponceDto);
//        }
//        return categoryResponceDtoList;
//
//    }

    @Override
    public CategoryPaginationResponce getAllCategories(Integer pageNumber, Integer pageSize , String sortby , String sortdirr) {

        Sort sort = sortdirr.equalsIgnoreCase("asc")
                ? Sort.by(sortby).ascending()
                : Sort.by(sortby).descending();

        Pageable pageable= PageRequest.of( pageNumber, pageSize , sort);
        Page<Category> categoryPage =categoryRepository.findAll(pageable);
        //ya categoryPage madhe sarv content ahe

        List<Category> categoryList =categoryPage.getContent();
        //sadhya content ky asnar list of cateory object with name n id
        //convert each category in list to CategoryResponceDto and add to CategoryResponceDtolists

        List<CategoryResponceDto> categoryResponceDtoList=new ArrayList<>();

        for( Category category: categoryList){
           CategoryResponceDto categoryResponceDto= modelMapper.map(category , CategoryResponceDto.class);
           categoryResponceDtoList.add(categoryResponceDto);
        }

       //built custom responce
        CategoryPaginationResponce responce=new CategoryPaginationResponce();
        responce.setContent(categoryResponceDtoList);
        responce.setPageNumber(categoryPage.getNumber());
        responce.setPageSize(categoryPage.getSize());
        responce.setTotalPages(categoryPage.getTotalPages());
        responce.setTotalElements((int) categoryPage.getTotalElements());
        responce.setLastPage(categoryPage.isLast());
        return responce;

    }

    @Override
    public CategoryResponceDto addNewCategory(CategoryRequestDto categoryRequestDto) {
        Category existingcateory = categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName());

        if(existingcateory!=null){
            //means category with same name exist already
            throw new ApiException("category with same name exist already");
        }
        //if not duplicate then save new one

        Category category =modelMapper.map(categoryRequestDto, Category.class);
        Category savedone= categoryRepository.save(category);
        return modelMapper.map(savedone ,CategoryResponceDto.class);


    }

    @Override
    public String deleteCategory(Long categoryid) {

        Category category = categoryRepository.findById(categoryid).orElse(null);

        if (category == null) {
            return "category with id:" + categoryid + " not found";
        }

        categoryRepository.deleteById(categoryid);
        return "category with id:" + categoryid + " deleted successfully";
    }

    @Override
    @Transactional
    public String updatecategoryobject(CategoryRequestDto categoryRequestDto, Long categoryid) {

      Category existingcategory=  categoryRepository.findById(categoryid).orElse(null);
      if(existingcategory==null){
          return "category not found with given id";
      }

      existingcategory.setCategoryName(categoryRequestDto.getCategoryName());
      categoryRepository.save(existingcategory);

        return "category update succesfully";
    }
}
