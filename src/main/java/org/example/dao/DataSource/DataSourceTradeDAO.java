package org.example.dao.DataSource;

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
        String sql = "INSERT INTO trades (user_sender, user_recipient, state) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, trade.getUserSender());
            stmt.setString(2, trade.getUserRecipient());
            stmt.setString(3, trade.getState());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trade.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Trade getTradeById(Long tradeId) {
        String sql = "SELECT * FROM trades WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, tradeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Trade trade = new Trade();
                    trade.setId(rs.getLong("id"));
                    trade.setUserSender(rs.getString("user_sender"));
                    trade.setUserRecipient(rs.getString("user_recipient"));
                    trade.setState(rs.getString("state"));
                    return trade;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateTrade(Trade trade) {
        String sql = "UPDATE trades SET user_sender = ?, user_recipient = ?, state = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trade.getUserSender());
            stmt.setString(2, trade.getUserRecipient());
            stmt.setString(3, trade.getState());
            stmt.setLong(4, trade.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTrade(Long tradeId) {
        String sql = "DELETE FROM trades WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, tradeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Trade> getAllTrades() {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM trades";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Trade trade = new Trade();
                trade.setId(rs.getLong("id"));
                trade.setUserSender(rs.getString("user_sender"));
                trade.setUserRecipient(rs.getString("user_recipient"));
                trade.setState(rs.getString("state"));
                trades.add(trade);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trades;
    }

    @Override
    public List<Trade> getTradesByUserSender(String userSender) {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM trades WHERE user_sender = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userSender);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trade trade = new Trade();
                    trade.setId(rs.getLong("id"));
                    trade.setUserSender(rs.getString("user_sender"));
                    trade.setUserRecipient(rs.getString("user_recipient"));
                    trade.setState(rs.getString("state"));
                    trades.add(trade);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trades;
    }

    @Override
    public List<Trade> getTradesByUserRecipient(String userRecipient) {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM trades WHERE user_recipient = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userRecipient);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trade trade = new Trade();
                    trade.setId(rs.getLong("id"));
                    trade.setUserSender(rs.getString("user_sender"));
                    trade.setUserRecipient(rs.getString("user_recipient"));
                    trade.setState(rs.getString("state"));
                    trades.add(trade);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trades;
    }
}
