package org.example.dao.Repository;

import org.example.beans.Role;
import org.example.dao.Interface.RoleDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository implements RoleDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addRole(Role role) {
        String sql = "INSERT INTO Role (Id, Name) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, role.getId());
            statement.setString(2, role.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении роли", e);
        }
    }

    @Override
    public Role getRoleById(Long roleId) {
        String sql = "SELECT * FROM Role WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, roleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRoleFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении роли по ID", e);
        }
        return null;
    }

    @Override
    public void updateRole(Role role) {
        String sql = "UPDATE Role SET Name = ? WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, role.getName());
            statement.setLong(2, role.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении роли", e);
        }
    }

    @Override
    public void deleteRole(Long roleId) {
        String sql = "DELETE FROM Role WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, roleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении роли", e);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                roles.add(mapRoleFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех ролей", e);
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

    private Role mapRoleFromResultSet(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("Id"));
        role.setName(resultSet.getString("Name"));
        return role;
    }
}