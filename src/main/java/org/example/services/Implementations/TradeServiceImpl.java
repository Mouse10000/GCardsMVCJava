package org.example.services.Implementations;


import org.example.beans.CardRecipient;
import org.example.beans.CardSender;
import org.example.beans.Trade;
import org.example.dao.Interface.CardDAO;
import org.example.dao.Interface.TradeDAO;
import org.example.dao.Interface.UserDAO;
import org.example.services.Interface.Exception.Trade.TradeNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.TradeService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Trade.InvalidTradeStateException;
import org.example.services.Interface.Exception.Trade.TradeException;

import java.util.List;

public class TradeServiceImpl implements TradeService {
    private final TradeDAO tradeDAO;
    private final CardDAO cardDAO;
    private final UserDAO userDAO;

    public TradeServiceImpl(TradeDAO tradeDAO, CardDAO cardDAO, UserDAO userDAO) {
        this.tradeDAO = tradeDAO;
        this.cardDAO = cardDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void createTrade(Trade trade, List<Long> senderCardIds, List<Long> recipientCardIds)
            throws TradeException, CardNotFoundException, UserNotFoundException {
        // Проверка существования пользователей
        if (userDAO.getUserById(trade.getUserSenderId()) == null) {
            throw new UserNotFoundException("Sender user with ID " + trade.getUserSenderId() + " not found.");
        }
        if (userDAO.getUserById(trade.getUserRecipientId()) == null) {
            throw new UserNotFoundException("Recipient user with ID " + trade.getUserRecipientId() + " not found.");
        }

        // Проверка существования карточек
        for (Long cardId : senderCardIds) {
            if (cardDAO.getCardById(cardId) == null) {
                throw new CardNotFoundException("Sender card with ID " + cardId + " not found.");
            }
        }
        for (Long cardId : recipientCardIds) {
            if (cardDAO.getCardById(cardId) == null) {
                throw new CardNotFoundException("Recipient card with ID " + cardId + " not found.");
            }
        }

        // Создание обмена
        tradeDAO.addTrade(trade);

        // Добавление карточек отправителя и получателя


        for (Long cardId : senderCardIds) {
            CardSender cardSender = new CardSender();
            cardSender.setTradeId(trade.getId());
            cardSender.setCardId(cardId);
            tradeDAO.addCardSender(cardSender);
        }

        for (Long cardId : recipientCardIds) {
            CardRecipient cardRecipient = new CardRecipient();
            cardRecipient.setTradeId(trade.getId());
            cardRecipient.setCardId(cardId);
            tradeDAO.addCardRecipient(cardRecipient);
        }
    }

    @Override
    public void updateTradeState(long tradeId, String state) throws TradeNotFoundException, InvalidTradeStateException {
        Trade trade = tradeDAO.getTradeById(tradeId);
        if (trade == null) {
            throw new TradeNotFoundException("Trade with ID " + tradeId + " not found.");
        }
        if (!List.of("pending", "completed", "cancelled").contains(state)) {
            throw new InvalidTradeStateException("Invalid trade state: " + state);
        }
        trade.setState(state);
        tradeDAO.updateTrade(trade);
    }

    @Override
    public Trade getTradeById(long tradeId) throws TradeNotFoundException {
        Trade trade = tradeDAO.getTradeById(tradeId);
        if (trade == null) {
            throw new TradeNotFoundException("Trade with ID " + tradeId + " not found.");
        }
        return trade;
    }

    @Override
    public List<Trade> getTradesByUser(long userId) throws UserNotFoundException {
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        return tradeDAO.getTradesByUserSender(userId);
    }

    @Override
    public void deleteTrade(long tradeId) throws TradeNotFoundException {
        if (tradeDAO.getTradeById(tradeId) == null) {
            throw new TradeNotFoundException("Trade with ID " + tradeId + " not found.");
        }
        tradeDAO.deleteTrade(tradeId);
    }
}
