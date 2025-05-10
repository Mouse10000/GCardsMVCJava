package org.example.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "userCard")
//@IdClass(UserRole.UserRoleId.class)
public class UserCard {

    @EmbeddedId
    private UserCardId userCardId;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "СardId")
    private Card card;

    private int CountDuplicate;
    public UserCard() {
    }

    public UserCard(User user, Card card, int countDuplicate) {
        this.user = user;
        this.card = card;
        CountDuplicate = countDuplicate;
        this.userCardId = new UserCardId(user.getId(), card.getId());
    }

    public UserCard(User user, Card card) {
        this.user = user;
        this.card = card;
        this.userCardId = new UserCardId(user.getId(), card.getId());
    }
    public User getUser() {
        return user;
    }

    /*public void setUser(User user) {
        this.user = user;
        this.userRoleId = user != null ? user.getId() : null;
    }*/

    public Card getCard() {
        return card;
    }

    public int getCountDuplicate() {
        return CountDuplicate;
    }

    public void setCountDuplicate(int countDuplicate) {
        CountDuplicate = countDuplicate;
    }

    /*public void setRole(Role role) {
        this.role = role;
        this.roleId = role != null ? role.getId() : null;
    }*/
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(userRoleId, userRole.userRoleId) &&
                Objects.equals(roleId, userRole.roleId);
    }*/
/*
    @Override
    public int hashCode() {
        return Objects.hash(userRoleId, roleId);
    }
*/
    @Embeddable
    public static class UserCardId implements Serializable {
        private static final long serialVersionUID = -4135407811430541370L;

        @Column(name = "UserId")
        private Long UserId;

        @Column(name = "CardId")
        private Long cardId;

        public UserCardId() {
        }

        public UserCardId(Long userId, Long cardId) {
            this.UserId = userId;
            this.cardId = cardId;
        }

        public Long getUserRoleId() {
            return UserId;
        }

        public void setUserRoleId(Long userId) {
            this.UserId = userId;
        }

        public Long getCardId() {
            return cardId;
        }

        public void setCardId(Long roleId) {
            this.cardId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserCardId)) return false;
            UserCardId that = (UserCardId) o;
            return Objects.equals(UserId, that.UserId) &&
                    Objects.equals(cardId, that.cardId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(UserId, cardId);
        }
    }
}