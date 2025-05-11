package org.example.services;

import org.example.model.*;
import org.example.repository.*;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Trade.InvalidTradeStateException;
import org.example.services.Interface.Exception.Trade.TradeException;
import org.example.services.Interface.Exception.Trade.TradeNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.TradeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TradeService implements TradeServiceInterface {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private CardSenderRepository cardSenderRepository;
    @Autowired
    private CardRecipientRepository cardRecipientRepository;

    @Override
    public Long initTrade(String userSenderName, String userRecipientName) throws UserNotFoundException, CardNotFoundException, TradeException {
        Optional<User> userSender = userRepository.findByUsername(userSenderName);
        if (userSender.isEmpty()) throw new UserNotFoundException("User Sender not found");
        Optional<User> userRecipient = userRepository.findByUsername(userRecipientName);
        if (userRecipient.isEmpty()) throw new UserNotFoundException("User Recipient not found");

        tradeRepository.save(new Trade(userSender.get(), userRecipient.get(), "init"));
        List<Trade> trades = tradeRepository.findAll();

        return trades.get(trades.size() - 1).getId();
    }

    @Override
    public void setSenderCard(Long tradeId, Long cardId) throws TradeNotFoundException, UserNotFoundException, CardNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");

        Optional<User> userSender = userRepository.findById(trade.get().getUserSender().getId());
        if (userSender.isEmpty()) throw new UserNotFoundException("User Sender not found");

        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty())
            throw new CardNotFoundException("Card with id " + cardId + " not found");

        userCardService.removeUserCard(userSender.get().getUsername(), cardBase.get().getId());
        cardSenderRepository.save(new CardSender(trade.get(), cardBase.get()));
        try {
            updateTradeState(tradeId, "setUserSenderCard");
        } catch (InvalidTradeStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSenderCard(Long tradeId, Long cardId) throws TradeNotFoundException, UserNotFoundException, CardNotFoundException {
        Optional<Trade> tradeBase = tradeRepository.findById(tradeId);
        if (tradeBase.isEmpty()) throw new TradeNotFoundException("Trade not found");

        Optional<User> userSender = userRepository.findById(tradeBase.get().getUserSender().getId());
        if (userSender.isEmpty()) throw new UserNotFoundException("User Sender not found");

        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty())
            throw new CardNotFoundException("Card with id " + cardId + " not found");

        userCardService.addUserCard(userSender.get().getUsername(), cardBase.get().getId());

        cardSenderRepository.deleteByTradeAndCard(tradeBase.get(), cardBase.get());
        try {
            updateTradeState(tradeId, "setUserSenderCard");
        } catch (InvalidTradeStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> getSenderCards(Long tradeId) throws TradeNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");

        return cardSenderRepository.getCardSenderByTrade(trade.get());
    }

    @Override
    public void setRecipientCard(Long tradeId, Long cardId) throws TradeNotFoundException, UserNotFoundException, CardNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");

        Optional<User> userRecipient = userRepository.findById(trade.get().getUserRecipient().getId());
        if (userRecipient.isEmpty()) throw new UserNotFoundException("User Recipient not found");

        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty())
            throw new CardNotFoundException("Card with id " + cardId + " not found");

        //userCardService.removeUserCard(userRecipient.get().getUsername(), cardBase.get().getId());
        cardRecipientRepository.save(new CardRecipient(trade.get(), cardBase.get()));
        try {
            updateTradeState(tradeId, "setUserRecipientCard");
        } catch (InvalidTradeStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRecipientCard(Long tradeId, Long cardId) throws TradeNotFoundException, UserNotFoundException, CardNotFoundException {
        Optional<Trade> tradeBase = tradeRepository.findById(tradeId);
        if (tradeBase.isEmpty()) throw new TradeNotFoundException("Trade not found");

        Optional<User> userRecipient = userRepository.findById(tradeBase.get().getUserRecipient().getId());
        if (userRecipient.isEmpty()) throw new UserNotFoundException("User Recipient not found");

        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty())
            throw new CardNotFoundException("Card with id " + cardId + " not found");

        //userCardService.addUserCard(userRecipient.get().getUsername(), cardBase.get().getId());

        cardRecipientRepository.deleteByTradeAndCard(tradeBase.get(), cardBase.get());
        try {
            updateTradeState(tradeId, "setUserRecipientCard");
        } catch (InvalidTradeStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> getRecipientCards(Long tradeId) throws TradeNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");

        return cardRecipientRepository.getCardRecipientByTrade(trade.get());
    }

    @Override
    public List<Card> getRecipientCardsNotInTrade(Long tradeId) throws TradeNotFoundException, CardNotFoundException, UserNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");
        User userRecipient = getUser(tradeId, "recipient");

        return cardRecipientRepository.findUserCardsNotInTrade(userRecipient, trade.get());
    }

    @Override
    public void createTrade(Long tradeId) throws TradeException, CardNotFoundException, UserNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");
        updateTradeState(tradeId, "post");
    }

    @Override
    public void submitTrade(Long tradeId) throws TradeException, UserNotFoundException, CardNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");

        List<Card> cardsSender = cardSenderRepository.getCardSenderByTrade(trade.get());
        List<Card> cardsRecipient = cardRecipientRepository.getCardRecipientByTrade(trade.get());
        String userSender = trade.get().getUserSender().getUsername();
        String userRecipient = trade.get().getUserRecipient().getUsername();

        userCardService.removeUserCards(userRecipient, cardsRecipient);
        userCardService.addUserCards(userSender, cardsRecipient);

        userCardService.addUserCards(userRecipient, cardsSender);

        updateTradeState(tradeId, "completedSuccess");
    }

    @Override
    public void cancelTrade(Long tradeId) throws TradeException, UserNotFoundException, CardNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");


        String userSender = trade.get().getUserSender().getUsername();
        List<Card> cardsSender = cardSenderRepository.getCardSenderByTrade(trade.get());
        userCardService.addUserCards(userSender, cardsSender);

        updateTradeState(tradeId, "completedCancel");
    }

    @Override
    public void updateTradeState(long tradeId, String state) throws TradeNotFoundException, InvalidTradeStateException, UserNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");
        List<String> tradeStates = Arrays.asList(
                "init", "setUserSenderCard", "setUserRecipientCard",
                "post", "completedCancel", "completedSuccess"
        );
        trade.get().setState(state);
        if (tradeStates.contains(state)) tradeRepository.save(trade.get());
        else throw new InvalidTradeStateException("Invalid Trade State");

    }

    @Override
    public Trade getTradeById(long tradeId) throws TradeNotFoundException {
        Optional<Trade> tradeBase = tradeRepository.findById(tradeId);
        if (tradeBase.isEmpty()) throw new TradeNotFoundException("Trade not found");

        return tradeBase.get();
    }

    @Override
    public Boolean tradePosted(long tradeId) throws TradeNotFoundException {
        Optional<Trade> tradeBase = tradeRepository.findById(tradeId);
        if (tradeBase.isEmpty()) throw new TradeNotFoundException("Trade not found");

        return tradeBase.get().getState().equals("post");
    }

    @Override
    public Boolean tradeCompleted(long tradeId) throws TradeNotFoundException {
        Optional<Trade> tradeBase = tradeRepository.findById(tradeId);
        if (tradeBase.isEmpty()) throw new TradeNotFoundException("Trade not found");

        return tradeBase.get().getState().contains("completed");
    }

    @Override
    public List<Trade> getTradesByUserSender(String username) throws UserNotFoundException {
        Optional<User> userBase = userRepository.findByUsername(username);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        return tradeRepository.findTradesByUserSender(userBase.get(), "completed");
    }

    @Override
    public List<Trade> getTradesByUserRecipient(String username) throws UserNotFoundException {
        Optional<User> userBase = userRepository.findByUsername(username);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        return tradeRepository.findTradesByUserRecipient(userBase.get(),"completed");
    }

    @Override
    public List<Trade> getTradesCompleted(String username) throws UserNotFoundException {
        Optional<User> userBase = userRepository.findByUsername(username);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        return tradeRepository.findAllByUserAndStateContains(
                userBase.get(), "complete");
    }

    @Override
    public User getUser(Long tradeId, String type) throws UserNotFoundException, TradeNotFoundException {
        Optional<Trade> tradeBase = tradeRepository.findById(tradeId);
        if (tradeBase.isEmpty()) throw new TradeNotFoundException("Trade not found");

        if(Objects.equals(type, "recipient"))
            return tradeBase.get().getUserRecipient();
        else
            return tradeBase.get().getUserSender();
    }

    @Override
    public void deleteTrade(long tradeId) throws TradeNotFoundException {
        Optional<Trade> trade = tradeRepository.findById(tradeId);
        if (trade.isEmpty()) throw new TradeNotFoundException("Trade not found");
        List<Card> cardsSender = cardSenderRepository.getCardSenderByTrade(trade.get());
        try {
            userCardService.addUserCards(trade.get().getUserSender().getUsername(), cardsSender);
        } catch (CardNotFoundException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        tradeRepository.deleteById(tradeId);
    }
}
