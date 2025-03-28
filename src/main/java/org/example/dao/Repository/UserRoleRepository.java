package org.example.dao.Repository;

import org.example.dao.Interface.UserRoleDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.beans.Role;
import org.example.beans.User;

public class UserRoleRepository implements UserRoleDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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
    public void deleteUserRole(long userId, long roleId) {
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
    public List<User> getUsersByRoleId(long roleId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM User u JOIN UserRole ur ON u.Id = ur.UserId WHERE ur.RoleId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, roleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователей по роли", e);
        }
        return users;
    }

    private Role mapRoleFromResultSet(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("Id"));
        role.setName(resultSet.getString("Name"));
        return role;
    }

    private User mapUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("Id"));
        user.setUserName(resultSet.getString("UserName"));
        user.setEmail(resultSet.getString("Email"));
        user.setPasswordHash(resultSet.getString("PasswordHash"));
        return user;
    }
}
