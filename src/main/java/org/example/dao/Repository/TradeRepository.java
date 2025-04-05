package org.example.dao.Repository;

import org.example.beans.CardRecipient;
import org.example.beans.CardSender;
import org.example.beans.Trade;
import org.example.dao.Interface.TradeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TradeRepository implements TradeDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Trade> tradeRowMapper = (rs, rowNum) -> {
        Trade trade = new Trade();
        trade.setId(rs.getLong("Id"));
        trade.setUserSenderId(rs.getLong("UserSenderId"));
        trade.setUserRecipientId(rs.getLong("UserRecipientId"));
        trade.setState(rs.getString("State"));
        return trade;
    };

    @Autowired
    public TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addTrade(Trade trade) {
        String sql = "INSERT INTO Trade (UserSenderId, UserRecipientId, State) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, trade.getUserSenderId());
            ps.setLong(2, trade.getUserRecipientId());
            ps.setString(3, trade.getState());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            trade.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public Trade getTradeById(Long tradeId) {
        String sql = "SELECT * FROM Trade WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, tradeRowMapper, tradeId);
    }

    @Override
    public void updateTrade(Trade trade) {
        String sql = "UPDATE Trade SET UserSenderId = ?, UserRecipientId = ?, State = ? WHERE Id = ?";
        jdbcTemplate.update(sql,
                trade.getUserSenderId(),
                trade.getUserRecipientId(),
                trade.getState(),
                trade.getId());
    }

    @Override
    public void deleteTrade(Long tradeId) {
        String sql = "DELETE FROM Trade WHERE Id = ?";
        jdbcTemplate.update(sql, tradeId);
    }

    @Override
    public List<Trade> getAllTrades() {
        String sql = "SELECT * FROM Trade";
        return jdbcTemplate.query(sql, tradeRowMapper);
    }

    @Override
    public List<Trade> getTradesByUserSender(Long userSenderId) {
        String sql = "SELECT * FROM Trade WHERE UserSenderId = ?";
        return jdbcTemplate.query(sql, tradeRowMapper, userSenderId);
    }

    @Override
    public List<Trade> getTradesByUserRecipient(Long userRecipientId) {
        String sql = "SELECT * FROM Trade WHERE UserRecipientId = ?";
        return jdbcTemplate.query(sql, tradeRowMapper, userRecipientId);
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
}