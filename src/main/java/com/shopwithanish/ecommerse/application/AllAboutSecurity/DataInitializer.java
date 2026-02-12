package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Model.Role;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.RoleRepository;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

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
    public void run(String... args) {

        // 1. Create roles
        createRoleIfMissing(AppRole.ADMIN);
        createRoleIfMissing(AppRole.USER);
        createRoleIfMissing(AppRole.SELLER);

        Role admin  = roleRepository.findByRoleName(AppRole.ADMIN).orElseThrow();
        Role user   = roleRepository.findByRoleName(AppRole.USER).orElseThrow();
        Role seller = roleRepository.findByRoleName(AppRole.SELLER).orElseThrow();

        // 2. Create users

        createUserIfMissing(
                "admin@shop.com",
                "Admin@123",
                "Admin",
                "7777777788",
                Set.of(admin, user, seller)
        );

        createUserIfMissing(
                "yash@shop.com",
                "yashbhai@123",
                "yashbhai",
                "7777077788",
                Set.of(admin, user, seller)
        );

        createUserIfMissing(
                "user1@shop.com",
                "user@1",
                "userone",
                "7707777788",
                Set.of(user)
        );

        createUserIfMissing(
                "user2@shop.com",
                "user@2",
                "usertwo",
                "7077777788",
                Set.of(user)
        );

        createUserIfMissing(
                "user3@shop.com",
                "user@3",
                "userThree",
                "7777777780",
                Set.of(user)
        );

        createUserIfMissing(
                "seller@shop.com",
                "Seller@123",
                "Seller",
                "7777777088",
                Set.of(seller)
        );

        createUserIfMissing(
                "seller2@shop.com",
                "Seller2@123",
                "Seller2",
                "7377777088",
                Set.of(seller)
        );

        System.out.println("Default users and roles initialized.");
    }

    private void createRoleIfMissing(AppRole roleName) {
        if (!roleRepository.existsByRoleName(roleName)) {
            Role role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
    }

    private void createUserIfMissing(String email,
                                     String password,
                                     String username,
                                     String phoneNo,
                                     Set<Role> roles) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        Users user = new Users();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNo(phoneNo);

        user.setRoles(roles);

        // If bidirectional
        for (Role role : roles) {
            role.getUsers().add(user);
        }

        userRepository.save(user);
        System.out.println("Created user: " + email);
    }
}
