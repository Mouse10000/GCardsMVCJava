package org.example.repository;

import org.example.model.Card;
import org.example.model.CardRecipient;
import org.example.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRecipientRepository  extends JpaRepository<CardRecipient, Long> {
    void deleteCardRecipientByCard(Optional<Card> cardBase);

    @Query("SELECT cs.card FROM CardRecipient cs WHERE cs.trade = :trade")
    List<Card> getCardRecipientByTrade(@Param("trade") Trade trade);

    @Transactional
    @Modifying
    @Query("DELETE FROM CardSender cs WHERE cs.trade = :trade AND cs.card = :card")
    void deleteByTradeAndCard(@Param("trade") Trade trade, @Param("card") Card card);
}
