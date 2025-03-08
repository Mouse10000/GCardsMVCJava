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
        String sql = "INSERT INTO CardRecipient (TradeId, CardId) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, cardRecipient.getTradeId());
            statement.setLong(2, cardRecipient.getCardId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cardRecipient.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении карточки-получателя", e);
        }
    }

    @Override
    public CardRecipient getCardRecipientById(int cardRecipientId) {
        String sql = "SELECT * FROM CardRecipient WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardRecipientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCardRecipientFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточки-получателя по ID", e);
        }
        return null;
    }

    @Override
    public void deleteCardRecipient(int cardRecipientId) {
        String sql = "DELETE FROM CardRecipient WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardRecipientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении карточки-получателя", e);
        }
    }

    @Override
    public List<CardRecipient> getAllCardRecipients() {
        List<CardRecipient> cardRecipients = new ArrayList<>();
        String sql = "SELECT * FROM CardRecipient";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                cardRecipients.add(mapCardRecipientFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех карточек-получателей", e);
        }
        return cardRecipients;
    }

    @Override
    public List<CardRecipient> getCardRecipientsByTradeId(Long tradeId) {
        List<CardRecipient> cardRecipients = new ArrayList<>();
        String sql = "SELECT * FROM CardRecipient WHERE TradeId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tradeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cardRecipients.add(mapCardRecipientFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточек-получателей по ID обмена", e);
        }
        return cardRecipients;
    }

    private CardRecipient mapCardRecipientFromResultSet(ResultSet resultSet) throws SQLException {
        CardRecipient cardRecipient = new CardRecipient();
        cardRecipient.setId(resultSet.getInt("Id"));
        cardRecipient.setTradeId(resultSet.getLong("TradeId"));
        cardRecipient.setCardId(resultSet.getLong("CardId"));
        return cardRecipient;
    }
}
