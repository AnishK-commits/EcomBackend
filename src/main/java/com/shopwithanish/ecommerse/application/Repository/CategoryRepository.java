package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category , Long> {



    Category findByCategoryName(@NonNull @NotBlank(message = "category_name should not be blank") String categoryName);


}
