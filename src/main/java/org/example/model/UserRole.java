package org.example.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий связь между пользователем и его ролью.
 * Определяет, какие роли назначены конкретному пользователю.
 */
@Entity
@Table(name = "userRole")
//@IdClass(UserRole.UserRoleId.class)
public class UserRole {

    @EmbeddedId
    private UserRoleId userRoleId;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private Role role;

    public UserRole() {
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.userRoleId = new UserRoleId(user.getId(), role.getId());
    }

    public User getUser() {
        return user;
    }

    /*public void setUser(User user) {
        this.user = user;
        this.userRoleId = user != null ? user.getId() : null;
    }*/

    public Role getRole() {
        return role;
    }

    @Embeddable
    public static class UserRoleId implements Serializable {
        private static final long serialVersionUID = -4135407811430541370L;

        @Column(name = "userId")
        private Long userId;

        @Column(name = "roleId")
        private Long roleId;

        public UserRoleId() {
        }

        public UserRoleId(Long userId, Long roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        public Long getUserRoleId() {
            return userId;
        }

        public void setUserRoleId(Long userId) {
            this.userId = userId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserRoleId)) return false;
            UserRoleId that = (UserRoleId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, roleId);
        }
    }
}