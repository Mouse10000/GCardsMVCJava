package org.example.beans;


public class UserCard extends BaseBean {
    private Long userId;
    private Long cardId;
    private int countDuplicate;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public int getCountDuplicate() {
        return countDuplicate;
    }

    public void setCountDuplicate(int countDuplicate) {
        this.countDuplicate = countDuplicate;
    }
}
