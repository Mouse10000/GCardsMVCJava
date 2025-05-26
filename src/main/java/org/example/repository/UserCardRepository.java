package org.example.repository;

import org.example.model.Card;
import org.example.model.Trade;
import org.example.model.User;
import org.example.model.UserCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, UserCard.UserCardId> {

    @Query("SELECT uc.card FROM UserCard uc WHERE uc.user = :user")
    List<Card> findCardsByUser(@Param("user") User user);

    @Query("SELECT uc.card FROM UserCard uc INNER JOIN CardRecipient cs WHERE cs.trade = :trade")
    List<Card> getUserRecipientCardsByTrade(@Param("trade") Trade trade);

    @Query("SELECT c FROM Card c WHERE c IN " +
            "(SELECT uc.card FROM UserCard uc WHERE uc.user = :user) " +
            "AND (:query IS null OR " +
            "c.name LIKE CONCAT('%', :query, '%')) " +
            "AND (:rank IS null OR c.cardRank = :rank) " +
            "AND (:minNumber IS null OR c.number >= :minNumber) " +
            "AND (:maxNumber IS null OR c.number <= :maxNumber)")
    Page<Card> findUserCardsByFilter(
            @Param("user") User user,
            @Param("query") String query,
            @Param("rank") String rank,
            @Param("minNumber") Integer minNumber,
            @Param("maxNumber") Integer maxNumber,
            Pageable pageable);
}
