package com.shopwithanish.ecommerse.application.Model;


import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    // category = ?
    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) ->
                cb.equal(root.get("category").get("categoryName"), category);
    }

    // name LIKE %keyword% OR description LIKE %keyword%
    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("productName")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    // category AND (name OR description)
    public static Specification<Product> categoryAndKeyword(
            String category,
            String keyword
    ) {
        return Specification
                .where(hasCategory(category))
                .and(hasKeyword(keyword));
    }
}