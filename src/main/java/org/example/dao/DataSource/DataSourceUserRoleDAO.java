package org.example.dao.DataSource;

import org.example.beans.UserRole;
import org.example.dao.Interface.UserRoleDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceUserRoleDAO implements UserRoleDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addUserRole(UserRole userRole) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userRole.getUserId());
            stmt.setString(2, userRole.getRoleId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserRole getUserRoleById(String userId, String roleId) {
        String sql = "SELECT * FROM user_roles WHERE user_id = ? AND role_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(rs.getString("user_id"));
                    userRole.setRoleId(rs.getString("role_id"));
                    return userRole;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateUserRole(UserRole userRole) {
        String sql = "UPDATE user_roles SET role_id = ? WHERE user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userRole.getRoleId());
            stmt.setString(2, userRole.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserRole(String userId, String roleId) {
        String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        List<UserRole> userRoles = new ArrayList<>();
        String sql = "SELECT * FROM user_roles";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(rs.getString("user_id"));
                userRole.setRoleId(rs.getString("role_id"));
                userRoles.add(userRole);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userRoles;
    }

    @Override
    public List<UserRole> getUserRolesByUserId(String userId) {
        List<UserRole> userRoles = new ArrayList<>();
        String sql = "SELECT * FROM user_roles WHERE user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(rs.getString("user_id"));
                    userRole.setRoleId(rs.getString("role_id"));
                    userRoles.add(userRole);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userRoles;
    }
}
