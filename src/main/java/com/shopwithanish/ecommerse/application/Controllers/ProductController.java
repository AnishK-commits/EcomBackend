package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AppConstants;
import com.shopwithanish.ecommerse.application.Model.ProductPaginationResponce;
import com.shopwithanish.ecommerse.application.Repository.CategoryRepository;
import com.shopwithanish.ecommerse.application.Repository.ProductRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.ProductRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.ProductResponceDto;
import com.shopwithanish.ecommerse.application.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @PostMapping("/add-new-prodct/category/{categoryid}")
    public ResponseEntity<ProductResponceDto> addNewProduct(@RequestBody ProductRequestDto productRequestDto,
                                                            @PathVariable Long categoryid) {
        return new ResponseEntity<>(productService.addNewProduct(productRequestDto, categoryid), HttpStatus.OK);
    }

    @GetMapping("/public/get-all-products")
    public ResponseEntity<ProductPaginationResponce> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                    @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYPRODUCT, required = false) String sortBy,
                                                                    @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        ProductPaginationResponce responce = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(responce, HttpStatus.FOUND);
    }

    @GetMapping("/public/get-product-by-category/{categoryid}")
    public ResponseEntity<ProductPaginationResponce> getProductsBycategory(@PathVariable Long categoryid , @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYPRODUCT, required = false) String sortBy,
                                                                          @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {
        ProductPaginationResponce productPaginationResponce = productService.getProductsBycategoryId(categoryid , pageNumber , pageSize , sortBy , sortOrder) ;
        return new ResponseEntity<>(productPaginationResponce, HttpStatus.FOUND);

    }

    @GetMapping("/public/find-products-by-keyword/{keywordd}")
    public ResponseEntity<ProductPaginationResponce> findProductsByKeyword(@PathVariable String keywordd , @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYPRODUCT, required = false) String sortBy,
                                                                          @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder) {

        ProductPaginationResponce productPaginationResponce  = productService.findProductsByKeyword(keywordd , pageNumber , pageSize , sortBy , sortOrder );
        return new ResponseEntity<>(productPaginationResponce, HttpStatus.FOUND);
    }


    @PutMapping("/update-existing-product/{productid}")
    public ResponseEntity<ProductResponceDto> updateExistingProductt(@RequestBody ProductRequestDto productRequestDto, @PathVariable Long productid) {
        ProductResponceDto productResponceDto = productService.updateExistingProductt(productRequestDto, productid);
        return new ResponseEntity<>(productResponceDto, HttpStatus.CREATED);
    }
    //when we update any product let say we update his price and inc/dec his price
    //then those changes should also reflect in cart of user who has this product in his cart and every user who had added this product
    //in his cart those changes should refect in every users cart
    //so first find all cart > listof cart who have this same product
    //same for deleteExistingProductM madhe pn product db madhub delete kela tr sarv carts madhun pn to automatically delete zala pahije

    @DeleteMapping("/delete-existing-product/{productid}")
    public ResponseEntity<String> deleteExistingProductM(@PathVariable Long productid) {
        return new ResponseEntity<>(productService.deleteExistingProductM(productid) , HttpStatus.OK);
    }



    @PutMapping(value = "/update-product-image/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponceDto> updateProductImagefn(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {

        ProductResponceDto response = productService.updateProductImagefn(productId, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}