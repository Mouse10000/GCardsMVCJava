package org.example.dao.repository;

import org.example.dao.Interface.CardRecipient;
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
 * Реализация интерфейса CardRecipientDAO для работы с карточками-получателями в базе данных.
 * Использует Spring JdbcTemplate для работы с БД.
 */
@Repository
public class CardRecipientRepository implements CardRecipient {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CardRecipientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<org.example.beans.CardRecipient> rowMapper = (rs, rowNum) -> {
        org.example.beans.CardRecipient cardRecipient = new org.example.beans.CardRecipient();
        cardRecipient.setId(rs.getInt("Id"));
        cardRecipient.setTradeId(rs.getLong("TradeId"));
        cardRecipient.setCardId(rs.getLong("CardId"));
        return cardRecipient;
    };

    @Override
    public void addCardRecipient(org.example.beans.CardRecipient cardRecipient) {
        String sql = "INSERT INTO CardRecipient (TradeId, CardId) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, cardRecipient.getTradeId());
            ps.setLong(2, cardRecipient.getCardId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            cardRecipient.setId(keyHolder.getKey().intValue());
        }
    }

    @Override
    public org.example.beans.CardRecipient getCardRecipientById(int cardRecipientId) {
        String sql = "SELECT * FROM CardRecipient WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, cardRecipientId);
    }

    @Override
    public void deleteCardRecipient(int cardRecipientId) {
        String sql = "DELETE FROM CardRecipient WHERE Id = ?";
        jdbcTemplate.update(sql, cardRecipientId);
    }

    @Override
    public List<org.example.beans.CardRecipient> getAllCardRecipients() {
        String sql = "SELECT * FROM CardRecipient";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<org.example.beans.CardRecipient> getCardRecipientsByTradeId(Long tradeId) {
        String sql = "SELECT * FROM CardRecipient WHERE TradeId = ?";
        return jdbcTemplate.query(sql, rowMapper, tradeId);
    }
}