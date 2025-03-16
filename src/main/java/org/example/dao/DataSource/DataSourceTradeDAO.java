package org.example.dao.DataSource;

import org.example.beans.CardRecipient;
import org.example.beans.CardSender;
import org.example.beans.Trade;
import org.example.dao.Interface.TradeDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceTradeDAO implements TradeDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addTrade(Trade trade) {
        String sql = "INSERT INTO Trade (UserSenderId, UserRecipientId, State) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, trade.getUserSenderId());
            statement.setLong(2, trade.getUserRecipientId());
            statement.setString(3, trade.getState());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trade.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении обмена", e);
        }
    }

    @Override
    public Trade getTradeById(Long tradeId) {
        String sql = "SELECT * FROM Trade WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tradeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapTradeFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении обмена по ID", e);
        }
        return null;
    }

    @Override
    public void updateTrade(Trade trade) {
        String sql = "UPDATE Trade SET UserSenderId = ?, UserRecipientId = ?, State = ? WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, trade.getUserSenderId());
            statement.setLong(2, trade.getUserRecipientId());
            statement.setString(3, trade.getState());
            statement.setLong(4, trade.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении обмена", e);
        }
    }

    @Override
    public void deleteTrade(Long tradeId) {
        String sql = "DELETE FROM Trade WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tradeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении обмена", e);
        }
    }

    @Override
    public List<Trade> getAllTrades() {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM Trade";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trades.add(mapTradeFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех обменов", e);
        }
        return trades;
    }

    @Override
    public List<Trade> getTradesByUserSender(Long userSenderId) {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM Trade WHERE UserSenderId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userSenderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trades.add(mapTradeFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении обменов по отправителю", e);
        }
        return trades;
    }

    @Override
    public List<Trade> getTradesByUserRecipient(Long userRecipientId) {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM Trade WHERE UserRecipientId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userRecipientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trades.add(mapTradeFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении обменов по получателю", e);
        }
        return trades;
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
            throw new RuntimeException("Ошибка при добавлении карточки-отправителя", e);
        }
    }

    private Trade mapTradeFromResultSet(ResultSet resultSet) throws SQLException {
        Trade trade = new Trade();
        trade.setId(resultSet.getLong("Id"));
        trade.setUserSenderId(resultSet.getLong("UserSenderId"));
        trade.setUserRecipientId(resultSet.getLong("UserRecipientId"));
        trade.setState(resultSet.getString("State"));
        return trade;
    }
}