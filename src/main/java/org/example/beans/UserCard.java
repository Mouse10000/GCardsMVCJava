package org.example.beans;


public class UserCard extends BaseBean {
    private String userName;
    private Long cardId;
    private int countDuplicate;

    // Getters and Setters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
