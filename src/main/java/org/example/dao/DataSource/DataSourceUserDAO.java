package org.example.dao.DataSource;

import org.example.beans.User;
import org.example.beans.UserCard;
import org.example.dao.Interface.UserDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.beans.Card;
import org.example.beans.Role;

public class DataSourceUserDAO implements UserDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO User (Id, UserName, Email, PasswordHash) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, user.getId());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPasswordHash());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении пользователя", e);
        }
    }

    @Override
    public User getUserById(long userId) {
        String sql = "SELECT * FROM User WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователя по ID", e);
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE User SET UserName = ?, Email = ?, PasswordHash = ? WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setLong(4, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public void deleteUser(long userId) {
        String sql = "DELETE FROM User WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех пользователей", e);
        }
        return users;
    }

    @Override
    public User getUserByUserName(String userName) {
        String sql = "SELECT * FROM User WHERE UserName = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователя по имени", e);
        }
        return null;
    }

    @Override
    public List<Card> getCardsByUserId(long userId) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT c.* FROM Card c JOIN UserCard uc ON c.Id = uc.CardId WHERE uc.UserId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cards.add(mapCardFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточек пользователя", e);
        }
        return cards;
    }

    @Override
    public List<Role> getRolesByUserId(long userId) {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.* FROM Role r JOIN UserRole ur ON r.Id = ur.RoleId WHERE ur.UserId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(mapRoleFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении ролей пользователя", e);
        }
        return roles;
    }

    @Override
    public void addUserRole(long userId, long roleId) {
        String sql = "INSERT INTO UserRole (UserId, RoleId) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, roleId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении связи пользователь-роль", e);
        }
    }

    @Override
    public void removeUserRole(long userId, long roleId) {
        String sql = "DELETE FROM UserRole WHERE UserId = ? AND RoleId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, roleId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении связи пользователь-роль", e);
        }
    }

    @Override
    public void addUserCard(UserCard userCard){
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

    private User mapUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("Id"));
        user.setUserName(resultSet.getString("UserName"));
        user.setEmail(resultSet.getString("Email"));
        user.setPasswordHash(resultSet.getString("PasswordHash"));
        return user;
    }

    private Card mapCardFromResultSet(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getLong("Id"));
        card.setName(resultSet.getString("Name"));
        card.setDateOfAdd(resultSet.getTimestamp("DateOfAdd").toLocalDateTime());
        card.setDescription(resultSet.getString("Description"));
        card.setImage(resultSet.getBytes("Image"));
        card.setRank(resultSet.getString("CardRank"));
        card.setNumber(resultSet.getInt("Number"));
        return card;
    }

    private Role mapRoleFromResultSet(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("Id"));
        role.setName(resultSet.getString("Name"));
        return role;
    }
}