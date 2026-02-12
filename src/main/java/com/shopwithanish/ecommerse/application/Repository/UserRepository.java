package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<Users , Long> {

    Optional<Users> findByUsername(String username);



    Optional<Users>  findByEmail(@NotBlank @Size(min = 5) String username);

    boolean existsByEmail(String email);

    @Query("""
        SELECT DISTINCT u
        FROM Users u
        JOIN u.roles r
        WHERE r.roleName = :roleName
    """)
    Page<Users> findUsersByRole(
                            @Param("roleName") AppRole roleName,
                            Pageable pageable
    );
}

//JOIN u.roles r → only users having roles
// DISTINCT → avoids duplicate users
// Pagination works correctly