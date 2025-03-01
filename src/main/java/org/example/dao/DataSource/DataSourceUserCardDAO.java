package org.example.dao.DataSource;

import org.example.beans.UserCard;
import org.example.dao.Interface.UserCardDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceUserCardDAO implements UserCardDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addUserCard(UserCard userCard) {
        String sql = "INSERT INTO user_cards (user_name, card_id, count_duplicate) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userCard.getUserName());
            stmt.setLong(2, userCard.getCardId());
            stmt.setInt(3, userCard.getCountDuplicate());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userCard.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserCard getUserCardById(Long userCardId) {
        String sql = "SELECT * FROM user_cards WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userCardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserCard userCard = new UserCard();
                    userCard.setId(rs.getLong("id"));
                    userCard.setUserName(rs.getString("user_name"));
                    userCard.setCardId(rs.getLong("card_id"));
                    userCard.setCountDuplicate(rs.getInt("count_duplicate"));
                    return userCard;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateUserCard(UserCard userCard) {
        String sql = "UPDATE user_cards SET user_name = ?, card_id = ?, count_duplicate = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userCard.getUserName());
            stmt.setLong(2, userCard.getCardId());
            stmt.setInt(3, userCard.getCountDuplicate());
            stmt.setLong(4, userCard.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserCard(Long userCardId) {
        String sql = "DELETE FROM user_cards WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userCardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserCard> getAllUserCards() {
        List<UserCard> userCards = new ArrayList<>();
        String sql = "SELECT * FROM user_cards";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserCard userCard = new UserCard();
                userCard.setId(rs.getLong("id"));
                userCard.setUserName(rs.getString("user_name"));
                userCard.setCardId(rs.getLong("card_id"));
                userCard.setCountDuplicate(rs.getInt("count_duplicate"));
                userCards.add(userCard);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userCards;
    }

    @Override
    public List<UserCard> getUserCardsByUserName(String userName) {
        List<UserCard> userCards = new ArrayList<>();
        String sql = "SELECT * FROM user_cards WHERE user_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UserCard userCard = new UserCard();
                    userCard.setId(rs.getLong("id"));
                    userCard.setUserName(rs.getString("user_name"));
                    userCard.setCardId(rs.getLong("card_id"));
                    userCard.setCountDuplicate(rs.getInt("count_duplicate"));
                    userCards.add(userCard);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userCards;
    }
}
