package org.example.services.Implementations;


import org.example.beans.Card;
import org.example.dao.Interface.CardDAO;
import org.example.services.Interface.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final CardDAO cardDAO;

    public CardServiceImpl(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    @Override
    public void addCard(Card card) throws DuplicateCardException, InvalidCardException {
        if (card.getName() == null || card.getName().isEmpty()) {
            throw new InvalidCardException("Card name cannot be empty.");
        }
        if (cardDAO.getCardByName(card.getName()) == null) {
            throw new DuplicateCardException("Card with name " + card.getName() + " already exists.");
        }
        cardDAO.addCard(card);
    }

    @Override
    public Card getCardById(long cardId) throws CardNotFoundException {
        Card card = cardDAO.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card with ID " + cardId + " not found.");
        }
        return card;
    }

    @Override
    public List<Card> getCardsByName(String name) throws CardNotFoundException {
        List<Card> cards = cardDAO.getCardByName(name); // Предполагаем, что cardDAO.getCardsByName возвращает список карт
        if (cards == null || cards.isEmpty()) {
            throw new CardNotFoundException("Card with name " + name + " not found.");
        }
        return cards; // Возвращаем список карт
    }

    @Override
    public void updateCard(Card card) throws CardNotFoundException, InvalidCardException {
        if (cardDAO.getCardById(card.getId()) == null) {
            throw new CardNotFoundException("Card with ID " + card.getId() + " not found.");
        }
        if (card.getName() == null || card.getName().isEmpty()) {
            throw new InvalidCardException("Card name cannot be empty.");
        }
        cardDAO.updateCard(card);
    }

    @Override
    public void deleteCard(long cardId) throws CardNotFoundException {
        if (cardDAO.getCardById(cardId) == null) {
            throw new CardNotFoundException("Card with ID " + cardId + " not found.");
        }
        cardDAO.deleteCard(cardId);
    }

    @Override
    public List<Card> getAllCards() {
        return cardDAO.getAllCards();
    }
}
