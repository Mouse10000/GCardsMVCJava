package org.example.services.Implementations;

import org.example.models.Card;
import org.example.dao.CardRepository;
import org.example.services.Interface.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public void addCard(Card card){

        cardRepository.save(card);

    }

    @Override
    public Card getCardById(long cardId) throws CardNotFoundException {
        return null;
    }

    @Override
    public List<Card> getCardsByName(String name) throws CardNotFoundException {
        return List.of();
    }

    @Override
    public void updateCard(Card card) throws CardNotFoundException, InvalidCardException {
    }

    @Override
    public void deleteCard(long cardId) throws CardNotFoundException {

    }

    @Override
    public List<Card> getAllCards() {
        return List.of();
    }
}

