package org.example.model;

import javax.persistence.*;

/**
 * Модель карты, запрашиваемой получателем в обмене.
 * Связывает карту с обменом, в котором она запрашивается.
 */
@Entity
@Table(name = "cardRecipient")
public class CardRecipient {
    /**
     * Уникальный идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Обмен, в котором запрашивается карта
     */
    @ManyToOne
    @JoinColumn(name = "tradeId", nullable = false)
    private Trade trade;

    /**
     * Карта, запрашиваемая в обмене
     */
    @ManyToOne
    @JoinColumn(name = "cardId", nullable = false)
    private Card card;

    public CardRecipient(Trade trade, Card card) {
        this.trade = trade;
        this.card = card;
    }

    public CardRecipient() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
