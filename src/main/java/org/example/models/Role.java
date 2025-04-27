package org.example.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="Role")
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    public Role() {}

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        for (UserRole userRole : userRoles) {
            users.add(userRole.getUser());
        }
        return users;
    }

    public void addUser(User user) {
        UserRole userRole = new UserRole(user, this);
        userRoles.add(userRole);
    }

    public void removeUser(User user) {
        userRoles.removeIf(userRole -> userRole.getUser().equals(user));
    }

    public static Role createNewRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
