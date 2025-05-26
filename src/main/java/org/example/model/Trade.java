package org.example.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель обмена картами между пользователями.
 * Представляет информацию об обмене, включая отправителя, получателя и обмениваемые карты.
 */
@Entity(name = "trade")
@Table(name = "trade")
public class Trade {
    /**
     * Уникальный идентификатор обмена
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, инициировавший обмен
     */
    @ManyToOne
    @JoinColumn(name = "UserSender", nullable = false)
    private User userSender;

    /**
     * Пользователь, получающий карты в обмене
     */
    @ManyToOne
    @JoinColumn(name = "UserRecipient", nullable = false)
    private User userRecipient;

    /**
     * Карты, предлагаемые для обмена отправителем
     */
    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardSender> cardSenders = new HashSet<>();

    /**
     * Карты, запрашиваемые получателем в обмене
     */
    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardRecipient> cardRecipients = new HashSet<>();

    /**
     * Статус обмена (например, ожидает подтверждения, завершен, отменен)
     */
    @Column(nullable = false)
    private String state;

    public Trade(User userSender, User userRecipient, String state) {
        this.userSender = userSender;
        this.userRecipient = userRecipient;
        this.state = state;
    }

    public Trade() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public User getUserRecipient() {
        return userRecipient;
    }

    public void setUserRecipient(User userRecipient) {
        this.userRecipient = userRecipient;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<CardSender> getCardSenders() {
        return cardSenders;
    }

    public void setCardSenders(Set<CardSender> cardSenders) {
        this.cardSenders = cardSenders;
    }

    public Set<CardRecipient> getCardRecipients() {
        return cardRecipients;
    }

    public void setCardRecipients(Set<CardRecipient> cardRecipients) {
        this.cardRecipients = cardRecipients;
    }
}


