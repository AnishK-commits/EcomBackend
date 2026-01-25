package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.FILESRELATED.FileServiceClass;
import com.shopwithanish.ecommerse.application.Model.*;
import com.shopwithanish.ecommerse.application.Repository.CartItemRepository;
import com.shopwithanish.ecommerse.application.Repository.CartRepository;
import com.shopwithanish.ecommerse.application.Repository.CategoryRepository;
import com.shopwithanish.ecommerse.application.Repository.ProductRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.ProductRequestDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.ProductResponceDto;
import com.shopwithanish.ecommerse.application.Services.CartService;
import com.shopwithanish.ecommerse.application.Services.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;   // ✅ correct one


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileServiceClass fileServiceClass;

    @Value("${project.image}")
    private String path;

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public ProductResponceDto addNewProduct(ProductRequestDto dto, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException("Category not found: " + categoryId));

        // ✅ CHECK HERE
        boolean exists = productRepository.existsByProductNameIgnoreCaseAndCategory( dto.getProductName(), category);

        if (exists==true) { throw new ApiException("Product already exists with name '" + dto.getProductName() + "' in this category" );}

        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setImage("defalut.png");
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());

        Double price = product.getPrice() == null ? 0.0 : product.getPrice();
        Double discount = product.getDiscount() == null ? 0.0 : product.getDiscount();
        product.setSpecialPrice(price - (discount / 100.0) * price);

        // set owning side
        product.setCategory(category);

        // ensure category collection initialized (keeps in-memory object graph consistent)
        if (category.getProduct() == null) {
            category.setProduct(new ArrayList<>());
        }
        category.getProduct().add(product);

        // save owning side (Product) so FK and product fields persist
        Product saved = productRepository.save(product);
        categoryRepository.save(category);

        return modelMapper.map(saved, ProductResponceDto.class);
    }

    @Override
    public ProductPaginationResponce getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of( pageNumber, pageSize , sort);
        Page<Product> categoryPage =productRepository.findAll(pageable);
        //ya categoryPage madhe sarv content ahe

        List<Product> productList =categoryPage.getContent();
        //sadhya content ky asnar list of cateory object with name n id
        //convert each category in list to CategoryResponceDto and add to CategoryResponceDtolists

        List<ProductResponceDto> productResponceDtoList=new ArrayList<>();

        for( Product product: productList){
            ProductResponceDto productResponceDto= modelMapper.map(product , ProductResponceDto.class);
            productResponceDtoList.add(productResponceDto);
        }

        //built custom responce
        ProductPaginationResponce responce=new ProductPaginationResponce();
        responce.setContent(productResponceDtoList);
        responce.setPageNumber(categoryPage.getNumber());
        responce.setPageSize(categoryPage.getSize());
        responce.setTotalPages(categoryPage.getTotalPages());
        responce.setTotalElements((int) categoryPage.getTotalElements());
        responce.setLastPage(categoryPage.isLast());
        return responce;
    }

    @Override
    public ProductPaginationResponce getProductsBycategoryId(Long categoryid , Integer pageNumber , Integer pageSize , String sortBy , String sortOrder) {

    Category existingcategory= categoryRepository.findById(categoryid).orElse(null);
   if(existingcategory==null)throw new ApiException("invalid category id");

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of( pageNumber, pageSize , sort);
        Page<Product> categoryPage =productRepository.findByCategory( existingcategory, pageable);
        //ya categoryPage madhe sarv content ahe

        List<Product> productList =categoryPage.getContent();
        //sadhya content ky asnar list of cateory object with name n id
        //convert each category in list to CategoryResponceDto and add to CategoryResponceDtolists

        List<ProductResponceDto> productResponceDtoList=new ArrayList<>();

        for( Product product: productList){
            ProductResponceDto productResponceDto= modelMapper.map(product , ProductResponceDto.class);
            productResponceDtoList.add(productResponceDto);
        }

        //built custom responce
        ProductPaginationResponce responce=new ProductPaginationResponce();
        responce.setContent(productResponceDtoList);
        responce.setPageNumber(categoryPage.getNumber());
        responce.setPageSize(categoryPage.getSize());
        responce.setTotalPages(categoryPage.getTotalPages());
        responce.setTotalElements((int) categoryPage.getTotalElements());
        responce.setLastPage(categoryPage.isLast());
        return responce;
    }

    @Override
    public ProductPaginationResponce findProductsByKeyword(String keywordd , Integer pageNumber , Integer pageSize , String sortBy , String sortOrder) {


//        List<Product> productList =productRepository.findByProductNameLikeIgnoreCase( '%' + keywordd +'%');
//        List<ProductResponceDto> productResponceDtoList=new ArrayList<>();
//        for(Product p: productList){
//            ProductResponceDto productResponceDto =modelMapper.map(p , ProductResponceDto.class);
//            productResponceDtoList.add(productResponceDto);
//        }
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of( pageNumber, pageSize , sort);
        Page<Product> categoryPage =productRepository.findByProductNameLikeIgnoreCase('%' + keywordd + '%'  , pageable );
        //ya categoryPage madhe sarv content ahe

        List<Product> productList =categoryPage.getContent();
        //sadhya content ky asnar list of cateory object with name n id
        //convert each category in list to CategoryResponceDto and add to CategoryResponceDtolists

        List<ProductResponceDto> productResponceDtoList=new ArrayList<>();

        for( Product product: productList){
            ProductResponceDto productResponceDto= modelMapper.map(product , ProductResponceDto.class);
            productResponceDtoList.add(productResponceDto);
        }

        //built custom responce
        ProductPaginationResponce responce=new ProductPaginationResponce();
        responce.setContent(productResponceDtoList);
        responce.setPageNumber(categoryPage.getNumber());
        responce.setPageSize(categoryPage.getSize());
        responce.setTotalPages(categoryPage.getTotalPages());
        responce.setTotalElements((int) categoryPage.getTotalElements());
        responce.setLastPage(categoryPage.isLast());
        return responce;


    }

    @Override
    public ProductResponceDto updateExistingProductt(ProductRequestDto productRequestDto, Long productid) {
        //find product bu PID
      Product productfromDB=  productRepository.findById(productid).orElse(null);
      if(productfromDB==null)throw new ApiException("product not exist with given id");

      productfromDB.setDescription(productRequestDto.getDescription());
      productfromDB.setDiscount(productRequestDto.getDiscount());
      productfromDB.setProductName(productRequestDto.getProductName());
      productfromDB.setStock(productRequestDto.getStock());
      productfromDB.setPrice(productRequestDto.getPrice());

      Double price = productRequestDto.getPrice() == null ? 0.0 : productRequestDto.getPrice();
      Double discount = productRequestDto.getDiscount() == null ? 0.0 : productRequestDto.getDiscount();
      productfromDB.setSpecialPrice(price - (discount / 100.0) * price);

      productfromDB=  productRepository.save(productfromDB);

      // after updating product changes should reflect in all cart in which this product is added mainly price changes
      //first find all carts> i.e list of cart

      List< Cart> cartList = cartRepository.findCartsByProductId(productid);

      for(Cart cartt: cartList){
          //find linked cartitem>> linked as product
         CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId(productid , cartt.getCartId()); //we got our desired product
          //now we need to do changes in this cart item
          if(cartItem!=null){
              cartItem= cartService.updateThisCartItemFN( cartt  , cartItem ,productid );
          }
      }

      ProductResponceDto  productResponceDto= modelMapper.map(productfromDB , ProductResponceDto.class);
      return productResponceDto;

    }


    @Override
    @Transactional
    public String deleteExistingProductM(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Product not exist with given product id"));

        // Remove product from all carts
        List<Cart> cartList = cartRepository.findCartsByProductId(productId);

        for (Cart cart : cartList) {
            cart.getCartItemList()
                    .removeIf(item -> item.getProduct().getProductId().equals(productId));
        }

        // Remove product from category
        Category category = product.getCategory();
        if (category != null) {
            category.getProduct().remove(product);
        }

        // Delete product
        productRepository.delete(product);

        return "Product Deleted";
    }

    @Override
    public ProductResponceDto updateProductImagefn(Long productid, MultipartFile image) throws IOException {
        Product productformDB = productRepository.findById(productid).orElse(null);
        if (productformDB == null) throw new ApiException("invalid product id ");


        String filename = fileServiceClass.uploadFileFn(path, image);
        productformDB.setImage(filename);
        productRepository.save(productformDB);

        return modelMapper.map(productformDB, ProductResponceDto.class);
    }





}
