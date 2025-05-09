package org.example.repository;

import org.example.model.Card;
import org.example.model.User;
import org.example.model.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, UserCard.UserCardId> {

    @Query("SELECT uc.card FROM UserCard uc WHERE uc.user = :user")
    List<Card> findCardsByUser(@Param("user") User user);

}
