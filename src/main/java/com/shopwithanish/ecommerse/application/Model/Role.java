package com.shopwithanish.ecommerse.application.Model;


import com.shopwithanish.ecommerse.application.Enums.AppRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long roleid;

    @Enumerated(value = EnumType.STRING)
    private AppRole roleName;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private Set<Users> users = new HashSet<>();


}
