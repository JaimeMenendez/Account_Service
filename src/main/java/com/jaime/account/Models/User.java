package com.jaime.account.Models;

import com.jaime.account.util.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private String lastname;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @OneToMany(targetEntity = Payment.class, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Payment> paymentList;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    Set<Role> roles = new HashSet<>();

    @Column
    @Builder.Default
    private boolean isAccountNonLocked = true;

    @Column
    @Builder.Default
    private int loginAttempts = 0;

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public Set<String> getRolesString() {
        return roles.stream().map(Enum::name).collect(Collectors.toSet());
    }
}
