package com.shopwithanish.ecommerse.application.AllAboutSecurity;


import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Model.Role;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.RoleRepository;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // -------------------------
        // 1. CREATE ROLES IF MISSING
        // -------------------------
        createRoleIfMissing(AppRole.ADMIN);
        createRoleIfMissing(AppRole.USER);
        createRoleIfMissing(AppRole.SELLER);

        Role admin  = roleRepository.findByRoleName(AppRole.ADMIN).get();
        Role user   = roleRepository.findByRoleName(AppRole.USER).get();
        Role seller = roleRepository.findByRoleName(AppRole.SELLER).get();


        // -------------------------
        // 2. CREATE DEFAULT USERS
        // -------------------------

        // Admin
        createUserIfMissing(
                "admin@shop.com",
                "Admin@123",
                "Admin User",
                admin
        );

        // 3 normal users
        createUserIfMissing(
                "user1@shop.com",
                "user@1",
                "userone",
                user
        );
        createUserIfMissing(
                "user2@shop.com",
                "user@2",
                "usertwo",
                user
        );
        createUserIfMissing(
                "user3@shop.com",
                "user@3",
                "userThree",
                user
        );

        // Seller
        createUserIfMissing(
                "seller@shop.com",
                "Seller@123",
                "Seller User",
                seller
        );

        System.out.println("Default users and roles initialized.");
    }


    // -------------------------
    // Helper Methods
    // -------------------------

    private void createRoleIfMissing(AppRole roleName) {
        if (!roleRepository.existsByRoleName(roleName)) {
            Role role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
    }

    private void createUserIfMissing(String email, String password, String username, Role role) {

        if (userRepository.existsByEmail(email)) {
            return; // Prevent duplicate creation
        }

        Users user = new Users();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // assign role
        user.getRoles().add(role);
        role.getUsers().add(user); // if bidirectional

        userRepository.save(user);

        System.out.println("Created user: " + email);
    }

}
