package org.example.dao.Repository;

import org.example.beans.CardRecipient;
import org.example.dao.Interface.CardRecipientDAO;
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
public class CardRecipientRepository implements CardRecipientDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CardRecipientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CardRecipient> rowMapper = (rs, rowNum) -> {
        CardRecipient cardRecipient = new CardRecipient();
        cardRecipient.setId(rs.getInt("Id"));
        cardRecipient.setTradeId(rs.getLong("TradeId"));
        cardRecipient.setCardId(rs.getLong("CardId"));
        return cardRecipient;
    };

    @Override
    public void addCardRecipient(CardRecipient cardRecipient) {
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
    public CardRecipient getCardRecipientById(int cardRecipientId) {
        String sql = "SELECT * FROM CardRecipient WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, cardRecipientId);
    }

    @Override
    public void deleteCardRecipient(int cardRecipientId) {
        String sql = "DELETE FROM CardRecipient WHERE Id = ?";
        jdbcTemplate.update(sql, cardRecipientId);
    }

    @Override
    public List<CardRecipient> getAllCardRecipients() {
        String sql = "SELECT * FROM CardRecipient";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<CardRecipient> getCardRecipientsByTradeId(Long tradeId) {
        String sql = "SELECT * FROM CardRecipient WHERE TradeId = ?";
        return jdbcTemplate.query(sql, rowMapper, tradeId);
    }
}