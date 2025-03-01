package org.example.dao.Interface;

import org.example.beans.CardRecipient;

import java.util.List;

public interface CardRecipientDAO {
    void addCardRecipient(CardRecipient cardRecipient);
    CardRecipient getCardRecipientById(int cardRecipientId);
    void updateCardRecipient(CardRecipient cardRecipient);
    void deleteCardRecipient(int cardRecipientId);
    List<CardRecipient> getAllCardRecipients();
    List<CardRecipient> getCardRecipientsByTradeId(Long tradeId);
}
