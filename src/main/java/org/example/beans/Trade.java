package org.example.beans;


public class Trade extends BaseBean {
    private String userSender;
    private String userRecipient;
    private String state;

    // Getters and Setters

    public String getUserSender() {
        return userSender;
    }

    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    public String getUserRecipient() {
        return userRecipient;
    }

    public void setUserRecipient(String userRecipient) {
        this.userRecipient = userRecipient;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
