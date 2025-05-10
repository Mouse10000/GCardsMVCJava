package org.example.model;

import javax.persistence.*;

@Entity
@Table(name = "CardSender")
public class CardSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TradeId", nullable = false)
    private Trade trade;

    @ManyToOne
    @JoinColumn(name = "CardId", nullable = false)
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
