package org.example.dao.Interface;

import org.example.beans.CardRecipient;
import org.example.beans.CardSender;

import java.util.List;

public interface Trade {
    void addTrade(org.example.beans.Trade trade);
    org.example.beans.Trade getTradeById(Long tradeId);
    void updateTrade(org.example.beans.Trade trade);
    void deleteTrade(Long tradeId);
    List<org.example.beans.Trade> getAllTrades();
    List<org.example.beans.Trade> getTradesByUserSender(Long userSenderId);
    List<org.example.beans.Trade> getTradesByUserRecipient(Long userRecipientId);

    void addCardSender(CardSender cardSender);
    void addCardRecipient(CardRecipient cardRecipient);
}