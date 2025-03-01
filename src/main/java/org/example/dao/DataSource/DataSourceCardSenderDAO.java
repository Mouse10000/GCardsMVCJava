package org.example.dao.DataSource;

import org.example.beans.CardSender;
import org.example.dao.Interface.CardSenderDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceCardSenderDAO implements CardSenderDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCardSender(CardSender cardSender) {
        String sql = "INSERT INTO card_senders (trade_id, card_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, cardSender.getTradeId());
            stmt.setLong(2, cardSender.getCardId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cardSender.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CardSender getCardSenderById(int cardSenderId) {
        String sql = "SELECT * FROM card_senders WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardSenderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CardSender cardSender = new CardSender();
                    cardSender.setId(rs.getInt("id"));
                    cardSender.setTradeId(rs.getLong("trade_id"));
                    cardSender.setCardId(rs.getLong("card_id"));
                    return cardSender;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateCardSender(CardSender cardSender) {
        String sql = "UPDATE card_senders SET trade_id = ?, card_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardSender.getTradeId());
            stmt.setLong(2, cardSender.getCardId());
            stmt.setLong(3, cardSender.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCardSender(int cardSenderId) {
        String sql = "DELETE FROM card_senders WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardSenderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CardSender> getAllCardSenders() {
        List<CardSender> cardSenders = new ArrayList<>();
        String sql = "SELECT * FROM card_senders";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CardSender cardSender = new CardSender();
                cardSender.setId(rs.getInt("id"));
                cardSender.setTradeId(rs.getLong("trade_id"));
                cardSender.setCardId(rs.getLong("card_id"));
                cardSenders.add(cardSender);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cardSenders;
    }

    @Override
    public List<CardSender> getCardSendersByTradeId(Long tradeId) {
        List<CardSender> cardSenders = new ArrayList<>();
        String sql = "SELECT * FROM card_senders WHERE trade_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, tradeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CardSender cardSender = new CardSender();
                    cardSender.setId(rs.getInt("id"));
                    cardSender.setTradeId(rs.getLong("trade_id"));
                    cardSender.setCardId(rs.getLong("card_id"));
                    cardSenders.add(cardSender);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cardSenders;
    }
}
