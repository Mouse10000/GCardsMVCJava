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
        String sql = "INSERT INTO UserCard (UserId, CardId, CountDuplicate) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, userCard.getUserId());
            statement.setLong(2, userCard.getCardId());
            statement.setInt(3, userCard.getCountDuplicate());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userCard.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении карточки пользователя", e);
        }
    }

    @Override
    public UserCard getUserCardById(Long userCardId) {
        String sql = "SELECT * FROM UserCard WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userCardId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUserCardFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточки пользователя по ID", e);
        }
        return null;
    }

    @Override
    public void updateUserCard(UserCard userCard) {
        String sql = "UPDATE UserCard SET UserId = ?, CardId = ?, CountDuplicate = ? WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userCard.getUserId());
            statement.setLong(2, userCard.getCardId());
            statement.setInt(3, userCard.getCountDuplicate());
            statement.setLong(4, userCard.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении карточки пользователя", e);
        }
    }

    @Override
    public void deleteUserCard(Long userCardId) {
        String sql = "DELETE FROM UserCard WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userCardId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении карточки пользователя", e);
        }
    }

    @Override
    public List<UserCard> getAllUserCards() {
        List<UserCard> userCards = new ArrayList<>();
        String sql = "SELECT * FROM UserCard";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                userCards.add(mapUserCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех карточек пользователей", e);
        }
        return userCards;
    }

    @Override
    public List<UserCard> getUserCardsByUserId(Long userId) {
        List<UserCard> userCards = new ArrayList<>();
        String sql = "SELECT * FROM UserCard WHERE UserId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userCards.add(mapUserCardFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточек пользователя по ID", e);
        }
        return userCards;
    }

    private UserCard mapUserCardFromResultSet(ResultSet resultSet) throws SQLException {
        UserCard userCard = new UserCard();
        userCard.setId(resultSet.getLong("Id"));
        userCard.setUserId(resultSet.getLong("UserId"));
        userCard.setCardId(resultSet.getLong("CardId"));
        userCard.setCountDuplicate(resultSet.getInt("CountDuplicate"));
        return userCard;
    }
}