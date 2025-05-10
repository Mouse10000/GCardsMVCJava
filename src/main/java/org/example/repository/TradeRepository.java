package org.example.repository;

import org.example.model.Trade;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository  extends JpaRepository<Trade, Long> {

    List<Trade> getTradesByUserRecipient(User user);
    List<Trade> getTradesByUserSender(User user);

    List<Trade> findAllByState(String state);

    List<Trade> getTradesByUserSenderAndStateNot(User userSender, String state);

    List<Trade> getTradesByUserRecipientAndStateNot(User userRecipient, String state);
}
