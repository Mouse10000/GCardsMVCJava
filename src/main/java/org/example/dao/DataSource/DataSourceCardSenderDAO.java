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
        String sql = "INSERT INTO CardSender (TradeId, CardId) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, cardSender.getTradeId());
            statement.setLong(2, cardSender.getCardId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cardSender.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении карточки-отправителя", e);
        }
    }

    @Override
    public CardSender getCardSenderById(int cardSenderId) {
        String sql = "SELECT * FROM CardSender WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardSenderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCardSenderFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточки-отправителя по ID", e);
        }
        return null;
    }

    @Override
    public void deleteCardSender(int cardSenderId) {
        String sql = "DELETE FROM CardSender WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardSenderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении карточки-отправителя", e);
        }
    }

    @Override
    public List<CardSender> getAllCardSenders() {
        List<CardSender> cardSenders = new ArrayList<>();
        String sql = "SELECT * FROM CardSender";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                cardSenders.add(mapCardSenderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех карточек-отправителей", e);
        }
        return cardSenders;
    }

    @Override
    public List<CardSender> getCardSendersByTradeId(long tradeId) {
        List<CardSender> cardSenders = new ArrayList<>();
        String sql = "SELECT * FROM CardSender WHERE TradeId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tradeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cardSenders.add(mapCardSenderFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточек-отправителей по ID обмена", e);
        }
        return cardSenders;
    }

    private CardSender mapCardSenderFromResultSet(ResultSet resultSet) throws SQLException {
        CardSender cardSender = new CardSender();
        cardSender.setId(resultSet.getInt("Id"));
        cardSender.setTradeId(resultSet.getLong("TradeId"));
        cardSender.setCardId(resultSet.getLong("CardId"));
        return cardSender;
    }
}
