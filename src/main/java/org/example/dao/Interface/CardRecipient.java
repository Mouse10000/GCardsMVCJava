package org.example.dao.Interface;

import java.util.List;

public interface CardRecipient {
    void addCardRecipient(org.example.beans.CardRecipient cardRecipient);
    org.example.beans.CardRecipient getCardRecipientById(int cardRecipientId);
    void deleteCardRecipient(int cardRecipientId);
    List<org.example.beans.CardRecipient> getAllCardRecipients();
    List<org.example.beans.CardRecipient> getCardRecipientsByTradeId(Long tradeId);
}
