package org.example.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель роли пользователя в системе.
 * Определяет права доступа и возможности пользователя в системе.
 */
@Entity
@Table(name = "role")
public class Role {
    /**
     * Уникальный идентификатор роли
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название роли (например, ROLE_USER, ROLE_ADMIN)
     */
    @Column(unique = true, nullable = false)
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

    public void setUserRoles(Set<UserRole> userRole1s) {
        this.userRoles = userRole1s;
    }

    /**
     * Пользователи, имеющие данную роль
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        UserRole userRole = new UserRole(user, this);
        userRoles.add(userRole);
    }

    public void removeUser(User user) {
        userRoles.removeIf(userRole -> userRole.getUser().equals(user));
    }

    public static org.example.model.Role createNewRole(String name) {
        org.example.model.Role role = new org.example.model.Role();
        role.setName(name);
        return role;
    }
}

