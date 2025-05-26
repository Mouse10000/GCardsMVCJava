package org.example.model;

import javax.persistence.*;

/**
 * Модель карты, предлагаемой для обмена отправителем.
 * Связывает карту с обменом, в котором она предлагается.
 */
@Entity
@Table(name = "cardSender")
public class CardSender {
    /**
     * Уникальный идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Обмен, в котором предлагается карта
     */
    @ManyToOne
    @JoinColumn(name = "tradeId", nullable = false)
    private Trade trade;

    /**
     * Карта, предлагаемая для обмена
     */
    @ManyToOne
    @JoinColumn(name = "cardId", nullable = false)
    private Card card;


    public CardSender(Trade trade, Card card) {
        this.trade = trade;
        this.card = card;
    }

    public CardSender() {}

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
