package org.example.beans;


public class Trade extends BaseBean {
    private Long userSenderId;
    private Long userRecipientId;
    private String state;

    // Getters and Setters

    public Long getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(Long userSenderId) {
        this.userSenderId = userSenderId;
    }

    public Long getUserRecipientId() {
        return userRecipientId;
    }

    public void setUserRecipientId(Long userRecipientId) {
        this.userRecipientId = userRecipientId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
