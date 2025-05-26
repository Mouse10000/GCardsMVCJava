package org.example.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель пользователя системы.
 * Представляет информацию о пользователе, включая его учетные данные и личную информацию.
 */
@Entity
@Table(name = "user")
public class User {
    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальное имя пользователя для входа в систему
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Пароль пользователя (хранится в зашифрованном виде)
     */
    @Column(nullable = false)
    private String password;

    /**
     * Электронная почта пользователя
     */
    @Column(unique = true)
    private String email;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Роли пользователя в системе
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Карты в коллекции пользователя
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserCard> userCards = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public void assignRole(Role role) {
        if (!hasRole(role)) {
            addRole(role);
        }
    }

    public void assignRoles(Set<Role> roles) {
        for (Role role : roles) {
            assignRole(role);
        }
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
}
