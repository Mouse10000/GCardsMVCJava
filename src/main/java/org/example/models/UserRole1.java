package org.example.models;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "userrole")
public class UserRole1 {
    @EmbeddedId
    @GeneratedValue(strategy = IDENTITY)
    private UserRoleId id;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User1 user;

    @ManyToOne
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private Role1 role;

    public UserRole1(User1 user, Role1 role) {
        this.user = user;
        this.role = role;
    }

    public UserRole1() {}

    public User1 getUser() {
        return user;
    }

    public void setUser(User1 user) {
        this.user = user;
    }

    public Role1 getRole() {
        return role;
    }

    public void setRole(Role1 role) {
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
