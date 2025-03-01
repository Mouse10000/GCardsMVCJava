package org.example.beans;


public class CardSender extends BaseBean {
    private Long tradeId;
    private Long cardId;

    // Getters and Setters

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
