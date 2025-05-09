package org.example.services.Interface;

import org.example.models.Card1;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService1 {
    void addCard(Card1 card1) throws DuplicateCardException, InvalidCardException;
    Card1 getCardById(long cardId) throws CardNotFoundException;
    List<Card1> getCardsByName(String name) throws CardNotFoundException;
    void updateCard(Card1 card1) throws CardNotFoundException, InvalidCardException;
    void deleteCard(long cardId) throws CardNotFoundException;
    List<Card1> getAllCards();

}