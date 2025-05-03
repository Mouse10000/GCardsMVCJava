package org.example.model;

import org.example.models.User1;
import org.example.models.UserRole1;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole1> userRole1s = new HashSet<>();

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

    public Set<UserRole1> getUserRoles() {
        return userRole1s;
    }

    public void setUserRoles(Set<UserRole1> userRole1s) {
        this.userRole1s = userRole1s;
    }

    public Set<User1> getUsers() {
        Set<User1> users = new HashSet<>();
        for (UserRole1 userRole1 : userRole1s) {
            users.add(userRole1.getUser());
        }
        return users;
    }

    /*public void addUser(User1 user) {
        UserRole1 userRole1 = new UserRole1(user, this);
        userRole1s.add(userRole1);
    }*/

    public void removeUser(User1 user) {
        userRole1s.removeIf(userRole1 -> userRole1.getUser().equals(user));
    }

    public static org.example.models.Role1 createNewRole(String name) {
        org.example.models.Role1 role = new org.example.models.Role1();
        role.setName(name);
        return role;
    }
}

