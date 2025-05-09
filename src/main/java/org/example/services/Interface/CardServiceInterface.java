package org.example.services.Interface;

import org.example.model.Card;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;

import java.util.List;

public interface CardServiceInterface {
    void addCard(Card card) throws DuplicateCardException, InvalidCardException;
    Card getCardById(long cardId) throws CardNotFoundException;
    List<Card> getCardsByName(String name) throws CardNotFoundException;
    void updateCard(Card card) throws CardNotFoundException, InvalidCardException;
    void deleteCard(long cardId) throws CardNotFoundException;
    List<Card> getAllCards();
    List<Card> getCardsOnPage(int page) throws CardNotFoundException;
    Card getCardByNumber(int number);
}
