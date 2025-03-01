package org.example.dao.Interface;

import org.example.beans.Card;

import java.util.List;

public interface CardDAO {
    void addCard(Card card);
    Card getCardById(Long cardId);
    void updateCard(Card card);
    void deleteCard(Long cardId);
    List<Card> getAllCards();
}
