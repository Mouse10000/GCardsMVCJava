package org.example.repository;

import org.example.model.Trade;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository  extends JpaRepository<Trade, Long> {

    List<Trade> getTradesByUserRecipient(User user);
    List<Trade> getTradesByUserSender(User user);

    List<Trade> findAllByState(String state);

    List<Trade> findAllByStateContains(String state);

    @Query("SELECT t FROM trade t WHERE " +
            "t.userSender = :user " +
            "AND t.state not LIKE %:state%")
    List<Trade> findTradesByUserSender(@Param("user") User user, @Param("state") String state);

    @Query("SELECT t FROM trade t WHERE " +
            "t.userRecipient = :user " +
            "AND t.state not LIKE %:state%")
    List<Trade> findTradesByUserRecipient(@Param("user") User user, @Param("state") String state);

    @Query("SELECT t FROM trade t WHERE " +
            "(t.userSender = :user OR t.userRecipient = :user) " +
            "AND t.state LIKE %:state%")
    List<Trade> findAllByUserAndStateContains(@Param("user") User user,
                                              @Param("state") String state);

}
