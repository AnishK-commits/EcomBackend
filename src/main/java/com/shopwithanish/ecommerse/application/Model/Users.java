package com.shopwithanish.ecommerse.application.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
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

    @Column(unique = true)
    @NotBlank
    @Size(max = 10)
    private String phoneNo;

    private String razorpayCustomerId;


    @JsonIgnore
    @JoinTable(
            name = "user_and_roleid",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Set<Role> roles=new HashSet<>();

    @ToString.Exclude
    //for seller dashboard when user will be seller
    @OneToMany( mappedBy ="seller" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE} , orphanRemoval = true)
    private List<Product> productList=new ArrayList<>();
    //normal user will no importance of productlist but when user is seller , he will have his own productlist, product should be related to seller
    //such that this product is from this seller ankush , this product is from this seller rushi.

    //users and address relationship

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Addresss> addresssList=new ArrayList<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "users")
    private Cart cart;

    @ToString.Exclude
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

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
