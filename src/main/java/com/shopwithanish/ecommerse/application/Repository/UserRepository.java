package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<Users , Long> {

    Optional<Users> findByUsername(String username);



    Optional<Users>  findByEmail(@NotBlank @Size(min = 5) String username);

    boolean existsByEmail(String email);
}
