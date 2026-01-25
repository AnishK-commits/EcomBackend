package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Category;
import com.shopwithanish.ecommerse.application.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {

   List<Product> findByCategory(Category existingcategory);

    Page<Product> findByCategory(Category existingcategory, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String s, Pageable pageable);

    boolean existsByProductNameIgnoreCaseAndCategory(String productName, Category category);
}
