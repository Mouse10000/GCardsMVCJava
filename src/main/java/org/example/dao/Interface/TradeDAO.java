package org.example.dao.Interface;

import org.example.beans.Trade;

import java.util.List;

public interface TradeDAO {
    void addTrade(Trade trade);
    Trade getTradeById(Long tradeId);
    void updateTrade(Trade trade);
    void deleteTrade(Long tradeId);
    List<Trade> getAllTrades();
    List<Trade> getTradesByUserSender(String userSender);
    List<Trade> getTradesByUserRecipient(String userRecipient);
}
