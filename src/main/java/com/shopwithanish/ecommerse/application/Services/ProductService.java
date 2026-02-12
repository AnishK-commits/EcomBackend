package com.shopwithanish.ecommerse.application.Services;

import com.shopwithanish.ecommerse.application.Model.ProductPaginationResponce;
import com.shopwithanish.ecommerse.application.RequestDtos.ProductRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.ProductResponceDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductResponceDto addNewProduct(ProductRequestDto productRequestDto, Long categoryid);


    ProductPaginationResponce getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String category, String keyword);

    ProductPaginationResponce getProductsBycategoryId(Long categoryid , Integer pageNumber , Integer pageSize , String sortBy , String sortOrder);

    ProductPaginationResponce findProductsByKeyword(String keywordd , Integer pageNumber ,Integer pageSize ,String sortBy , String sortOrder);

    ProductResponceDto updateExistingProductt(ProductRequestDto productRequestDto, Long productid);

    String deleteExistingProductM(Long productid);

    ProductResponceDto updateProductImagefn(Long productid, MultipartFile image) throws IOException;

    ProductPaginationResponce getAllProductsAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponceDto getProductDetailFromProductId(Long productID);

    ProductPaginationResponce getAllProductsAddedBySeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
