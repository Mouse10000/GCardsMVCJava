package org.example.dao.Interface;

import org.example.beans.CardSender;

import java.util.List;

public interface CardSenderDAO {
    void addCardSender(CardSender cardSender);
    CardSender getCardSenderById(int cardSenderId);
    void deleteCardSender(int cardSenderId);
    List<CardSender> getAllCardSenders();
    List<CardSender> getCardSendersByTradeId(long tradeId);
}