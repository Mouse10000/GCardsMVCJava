package org.example.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole1> userRole1s = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRole1> getUserRoles() {
        return userRole1s;
    }

    public void setUserRoles(Set<UserRole1> userRole1s) {
        this.userRole1s = userRole1s;
    }

    public Set<Role1> getRoles() {
        Set<Role1> roles = new HashSet<>();
        for (UserRole1 userRole1 : userRole1s) {
            roles.add(userRole1.getRole());
        }
        return roles;
    }

    public void addRole(Role1 role) {
        UserRole1 userRole1 = new UserRole1(this, role);
        userRole1s.add(userRole1);
    }

    public void removeRole(Role1 role) {
        userRole1s.removeIf(userRole1 -> userRole1.getRole().equals(role));
    }

    public void assignRole(Role1 role) {
        if (!hasRole(role)) {
            addRole(role);
        }
    }

    public void assignRoles(Set<Role1> roles) {
        for (Role1 role : roles) {
            assignRole(role);
        }
    }

    public boolean hasRole(Role1 role) {
        return getRoles().contains(role);
    }

    public boolean hasRole(String roleName) {
        return getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
}
