package org.example.model;

import org.example.model.Role;
import org.example.model.User;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "userrole")
public class UserRole {
    @EmbeddedId
    @GeneratedValue(strategy = IDENTITY)
    private org.example.models.UserRole1.UserRoleId id;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private Role role;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public UserRole() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Embeddable
    public static class UserRoleId implements Serializable {
        private static final long serialVersionUID = -4135407811430541370L;
        @Column(name = "UserId")
        protected Long noteId;
        @Column(name = "RoleId")
        protected Long roleId;

        protected UserRoleId() {
        }

        public UserRoleId(Long noteId, Long roleId) {
            this.noteId = noteId;
            this.roleId = roleId;
        }

        public Long getNoteId() {
            return noteId;
        }

        public void setNoteId(Long noteId) {
            this.noteId = noteId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long tagId) {
            this.roleId = tagId;
        }
    }
}
