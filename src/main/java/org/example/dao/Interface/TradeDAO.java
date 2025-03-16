package org.example.dao.Interface;

import org.example.beans.CardRecipient;
import org.example.beans.CardSender;
import org.example.beans.Trade;

import java.util.List;

public interface TradeDAO {
    void addTrade(Trade trade);
    Trade getTradeById(Long tradeId);
    void updateTrade(Trade trade);
    void deleteTrade(Long tradeId);
    List<Trade> getAllTrades();
    List<Trade> getTradesByUserSender(Long userSenderId);
    List<Trade> getTradesByUserRecipient(Long userRecipientId);

    void addCardSender(CardSender cardSender);
    void addCardRecipient(CardRecipient cardRecipient);
}