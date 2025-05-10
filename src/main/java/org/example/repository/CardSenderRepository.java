package org.example.repository;

import org.example.model.Card;
import org.example.model.CardSender;
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
public interface CardSenderRepository  extends JpaRepository<CardSender, Long> {

    @Query("SELECT cs.card FROM CardSender cs WHERE cs.trade = :trade")
    List<Card> getCardSenderByTrade(@Param("trade") Trade trade);

    // Метод для поиска конкретного CardSender по Trade и Card
    Optional<CardSender> findByTradeAndCard(Trade trade, Card card);

    // Метод для удаления CardSender по Trade и Card
    @Transactional
    @Modifying
    @Query("DELETE FROM CardSender cs WHERE cs.trade = :trade AND cs.card = :card")
    void deleteByTradeAndCard(@Param("trade") Trade trade, @Param("card") Card card);
}
