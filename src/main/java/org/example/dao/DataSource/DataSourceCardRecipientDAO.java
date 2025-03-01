package org.example.dao.DataSource;

import org.example.beans.CardRecipient;
import org.example.dao.Interface.CardRecipientDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceCardRecipientDAO implements CardRecipientDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCardRecipient(CardRecipient cardRecipient) {
        String sql = "INSERT INTO card_recipients (trade_id, card_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, cardRecipient.getTradeId());
            stmt.setLong(2, cardRecipient.getCardId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cardRecipient.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CardRecipient getCardRecipientById(int cardRecipientId) {
        String sql = "SELECT * FROM card_recipients WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardRecipientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CardRecipient cardRecipient = new CardRecipient();
                    cardRecipient.setId(rs.getInt("id"));
                    cardRecipient.setTradeId(rs.getLong("trade_id"));
                    cardRecipient.setCardId(rs.getLong("card_id"));
                    return cardRecipient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateCardRecipient(CardRecipient cardRecipient) {
        String sql = "UPDATE card_recipients SET trade_id = ?, card_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardRecipient.getTradeId());
            stmt.setLong(2, cardRecipient.getCardId());
            stmt.setLong(3, cardRecipient.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCardRecipient(int cardRecipientId) {
        String sql = "DELETE FROM card_recipients WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardRecipientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CardRecipient> getAllCardRecipients() {
        List<CardRecipient> cardRecipients = new ArrayList<>();
        String sql = "SELECT * FROM card_recipients";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CardRecipient cardRecipient = new CardRecipient();
                cardRecipient.setId(rs.getInt("id"));
                cardRecipient.setTradeId(rs.getLong("trade_id"));
                cardRecipient.setCardId(rs.getLong("card_id"));
                cardRecipients.add(cardRecipient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cardRecipients;
    }

    @Override
    public List<CardRecipient> getCardRecipientsByTradeId(Long tradeId) {
        List<CardRecipient> cardRecipients = new ArrayList<>();
        String sql = "SELECT * FROM card_recipients WHERE trade_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, tradeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CardRecipient cardRecipient = new CardRecipient();
                    cardRecipient.setId(rs.getInt("id"));
                    cardRecipient.setTradeId(rs.getLong("trade_id"));
                    cardRecipient.setCardId(rs.getLong("card_id"));
                    cardRecipients.add(cardRecipient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cardRecipients;
    }
}
