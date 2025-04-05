package org.example.dao.Repository;

import org.example.beans.CardSender;
import org.example.dao.Interface.CardSenderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Реализация интерфейса CardSenderDAO с использованием Spring JdbcTemplate
 */
@Repository
public class CardSenderRepository implements CardSenderDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CardSender> rowMapper = (rs, rowNum) -> {
        CardSender cardSender = new CardSender();
        cardSender.setId(rs.getInt("Id"));
        cardSender.setTradeId(rs.getLong("TradeId"));
        cardSender.setCardId(rs.getLong("CardId"));
        return cardSender;
    };

    @Autowired
    public CardSenderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addCardSender(CardSender cardSender) {
        String sql = "INSERT INTO CardSender (TradeId, CardId) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, cardSender.getTradeId());
            ps.setLong(2, cardSender.getCardId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            cardSender.setId(keyHolder.getKey().intValue());
        }
    }

    @Override
    public CardSender getCardSenderById(int cardSenderId) {
        String sql = "SELECT * FROM CardSender WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, cardSenderId);
    }

    @Override
    public void deleteCardSender(int cardSenderId) {
        String sql = "DELETE FROM CardSender WHERE Id = ?";
        jdbcTemplate.update(sql, cardSenderId);
    }

    @Override
    public List<CardSender> getAllCardSenders() {
        String sql = "SELECT * FROM CardSender";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<CardSender> getCardSendersByTradeId(long tradeId) {
        String sql = "SELECT * FROM CardSender WHERE TradeId = ?";
        return jdbcTemplate.query(sql, rowMapper, tradeId);
    }
}