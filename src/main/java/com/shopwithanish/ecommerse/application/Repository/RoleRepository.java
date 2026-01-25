package com.shopwithanish.ecommerse.application.Repository;

import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByRoleName(AppRole appRole);

    boolean existsByRoleName(AppRole roleName);
}