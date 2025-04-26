package org.example.dao.Interface;

import java.util.List;

public interface CardSender {
    void addCardSender(org.example.beans.CardSender cardSender);
    org.example.beans.CardSender getCardSenderById(int cardSenderId);
    void deleteCardSender(int cardSenderId);
    List<org.example.beans.CardSender> getAllCardSenders();
    List<org.example.beans.CardSender> getCardSendersByTradeId(long tradeId);
}