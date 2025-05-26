package org.example.services.Interface;

import org.example.model.Card;
import org.example.model.Trade;
import org.example.model.User;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Trade.InvalidTradeStateException;
import org.example.services.Interface.Exception.Trade.TradeException;
import org.example.services.Interface.Exception.Trade.TradeNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;

import java.util.List;

public interface TradeServiceInterface {

    Long initTrade(String userSenderName, String userRecipientName)
            throws UserNotFoundException, CardNotFoundException, TradeException;
    void setSenderCard(Long tradeId, Long cardId)
            throws TradeNotFoundException, UserNotFoundException, CardNotFoundException;
    void deleteSenderCard(Long tradeId, Long cardId)
            throws TradeNotFoundException, UserNotFoundException, CardNotFoundException;
    List<Card> getSenderCards(Long tradeId) throws  TradeNotFoundException;

    void setRecipientCard(Long tradeId, Long cardId)
            throws TradeNotFoundException, UserNotFoundException, CardNotFoundException;
    void deleteRecipientCard(Long tradeId, Long cardId)
            throws TradeNotFoundException, UserNotFoundException, CardNotFoundException;
    List<Card> getRecipientCards(Long tradeId) throws  TradeNotFoundException;
    List<Card> getRecipientCardsNotInTrade(Long tradeId) throws TradeNotFoundException, CardNotFoundException, UserNotFoundException;

    void createTrade(Long tradeId) throws TradeException, CardNotFoundException, UserNotFoundException;
    void submitTrade(Long tradeId) throws TradeException, UserNotFoundException, CardNotFoundException;
    void cancelTrade(Long tradeId) throws TradeException, UserNotFoundException, CardNotFoundException;

    void updateTradeState(long tradeId, String state) throws TradeNotFoundException, InvalidTradeStateException, UserNotFoundException;
    Trade getTradeById(long tradeId) throws TradeNotFoundException;
    Boolean tradePosted(long tradeId) throws TradeNotFoundException;
    Boolean tradeCompleted(long tradeId) throws TradeNotFoundException;

    List<Trade> getTradesByUserSender(String username) throws UserNotFoundException;
    List<Trade> getTradesByUserRecipient(String username) throws UserNotFoundException;
    List<Trade> getTradesCompleted(String username) throws UserNotFoundException;
    User getUser(Long tradeId, String type) throws TradeNotFoundException, UserNotFoundException;

    void deleteTrade(long tradeId) throws TradeNotFoundException;
}
