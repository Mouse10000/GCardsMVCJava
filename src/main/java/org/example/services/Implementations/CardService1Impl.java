package org.example.services.Implementations;

import org.example.models.Card1;
import org.example.dao.CardRepository1;
import org.example.services.Interface.CardService1;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService1Impl implements CardService1 {
    @Autowired
    private CardRepository1 cardRepository1;

    @Override
    public void addCard(Card1 card1){

        cardRepository1.save(card1);

    }

    @Override
    public Card1 getCardById(long cardId) throws CardNotFoundException {
        return null;
    }

    @Override
    public List<Card1> getCardsByName(String name) throws CardNotFoundException {
        return List.of();
    }

    @Override
    public void updateCard(Card1 card1) throws CardNotFoundException, InvalidCardException {
    }

    @Override
    public void deleteCard(long cardId) throws CardNotFoundException {

    }

    @Override
    public List<Card1> getAllCards() {
        return List.of();
    }
}

