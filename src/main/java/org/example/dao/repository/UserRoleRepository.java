package org.example.dao.repository;

import org.example.beans.Role;
import org.example.beans.User;
import org.example.dao.Interface.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRoleRepository implements UserRole {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Role> roleRowMapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(rs.getLong("Id"));
        role.setName(rs.getString("Name"));
        return role;
    };

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("Id"));
        user.setUserName(rs.getString("UserName"));
        user.setEmail(rs.getString("Email"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        return user;
    };

    @Autowired
    public UserRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUserRole(long userId, long roleId) {
        String sql = "INSERT INTO UserRole (UserId, RoleId) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, roleId);
    }

    @Override
    public void deleteUserRole(long userId, long roleId) {
        String sql = "DELETE FROM UserRole WHERE UserId = ? AND RoleId = ?";
        jdbcTemplate.update(sql, userId, roleId);
    }

    @Override
    public List<Role> getRolesByUserId(long userId) {
        String sql = "SELECT r.* FROM Role r JOIN UserRole ur ON r.Id = ur.RoleId WHERE ur.UserId = ?";
        return jdbcTemplate.query(sql, roleRowMapper, userId);
    }

    @Override
    public List<User> getUsersByRoleId(long roleId) {
        String sql = "SELECT u.* FROM User u JOIN UserRole ur ON u.Id = ur.UserId WHERE ur.RoleId = ?";
        return jdbcTemplate.query(sql, userRowMapper, roleId);
    }
}