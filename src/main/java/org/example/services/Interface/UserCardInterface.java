package org.example.services.Interface;

import org.example.model.Card;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;

import java.util.List;

public interface UserCardInterface {
    void addUserCard(String userName, long cardId) throws CardNotFoundException, UserNotFoundException;
    void addUserCards(String userName, List<Card> cards) throws CardNotFoundException, UserNotFoundException;

    List<Card> getCardsByName(String name) throws CardNotFoundException;
    void removeUserCard(String userName, long cardId) throws CardNotFoundException, UserNotFoundException;
    void removeUserCards(String userName, List<Card> cards) throws CardNotFoundException, UserNotFoundException;
    List<Card> getAllUserCards(String userName) throws UserNotFoundException;
    List<Card> getCardsOnPage(int page) throws CardNotFoundException;
    Card getCardByNumber(int number);
}
