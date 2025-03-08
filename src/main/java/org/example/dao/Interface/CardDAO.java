package org.example.dao.Interface;

import org.example.beans.Card;

import java.util.List;

public interface CardDAO {
    void addCard(Card card);
    Card getCardById(long cardId);
    List<Card> getCardByName(String name);
    void updateCard(Card card);
    void deleteCard(long cardId);
    List<Card> getAllCards();
}
