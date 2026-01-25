package com.shopwithanish.ecommerse.application.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userid;

    @Column(unique = true)
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Column(unique = true)
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;



    @JoinTable(
            name = "user_and_roleid",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Set<Role> roles=new HashSet<>();

    @ToString.Exclude
    //for seller dashboard when user will be seller
    @OneToMany( mappedBy ="user" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE} , orphanRemoval = true)
    private List<Product> productList=new ArrayList<>();


    //users and address relationship

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Addresss> addresssList=new ArrayList<>();

    @OneToOne(mappedBy = "users")
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //IF ONLY WANT ROLE BASED NO PERMISSION THEN THIS CODE
        return roles.stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getRoleName().name()))
              .collect(Collectors.toSet());

//        Set<GrantedAuthority> grantedAuthoritySet=new HashSet<>();
//
//        for( Role role : roles) {
//
//            //add role to set
//            grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
//
//            //add permission of that role in set
//            //find permission for role
//            Set<PermissionsTypes> permset = RoleandPermissionsMapping.getPermissionForParticularRole(role);
//
//            for (PermissionsTypes permissionxx : permset) {
//
//                grantedAuthoritySet.add(new SimpleGrantedAuthority(permissionxx.name()));
//
//            }
//        }
//        return grantedAuthoritySet;
    }
}
