package org.example.services.Interface;

import org.example.beans.Trade;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Trade.InvalidTradeStateException;
import org.example.services.Interface.Exception.Trade.TradeException;
import org.example.services.Interface.Exception.Trade.TradeNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;

import java.util.List;

public interface TradeService {
    void createTrade(Trade trade, List<Long> senderCardIds, List<Long> recipientCardIds)
            throws TradeException, CardNotFoundException, UserNotFoundException;
    void updateTradeState(long tradeId, String state) throws TradeNotFoundException, InvalidTradeStateException;
    Trade getTradeById(long tradeId) throws TradeNotFoundException;
    List<Trade> getTradesByUser(long userId) throws UserNotFoundException;
    void deleteTrade(long tradeId) throws TradeNotFoundException;
}
