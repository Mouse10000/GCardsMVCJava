package org.example.models;

import java.util.List;

public class TradeCreateRequest {
    private Long senderUserId;
    private Long recipientUserId;
    private List<Long> senderCardIds;
    private List<Long> recipientCardIds;

    // Геттеры и сеттеры
    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public List<Long> getSenderCardIds() {
        return senderCardIds;
    }

    public void setSenderCardIds(List<Long> senderCardIds) {
        this.senderCardIds = senderCardIds;
    }

    public List<Long> getRecipientCardIds() {
        return recipientCardIds;
    }

    public void setRecipientCardIds(List<Long> recipientCardIds) {
        this.recipientCardIds = recipientCardIds;
    }
}