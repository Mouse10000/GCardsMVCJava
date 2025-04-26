package org.example.dao.repository;

import org.example.dao.Interface.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Реализация интерфейса RoleDAO с использованием Spring JdbcTemplate
 */
@Repository
public class RoleRepository implements Role {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<org.example.beans.Role> roleRowMapper = (rs, rowNum) -> {
        org.example.beans.Role role = new org.example.beans.Role();
        role.setId(rs.getLong("Id"));
        role.setName(rs.getString("Name"));
        return role;
    };

    @Autowired
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addRole(org.example.beans.Role role) {
        String sql = "INSERT INTO Role (Id, Name) VALUES (?, ?)";
        jdbcTemplate.update(sql, role.getId(), role.getName());
    }

    @Override
    public org.example.beans.Role getRoleById(Long roleId) {
        String sql = "SELECT * FROM Role WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, roleRowMapper, roleId);
    }

    @Override
    public void updateRole(org.example.beans.Role role) {
        String sql = "UPDATE Role SET Name = ? WHERE Id = ?";
        jdbcTemplate.update(sql, role.getName(), role.getId());
    }

    @Override
    public void deleteRole(Long roleId) {
        String sql = "DELETE FROM Role WHERE Id = ?";
        jdbcTemplate.update(sql, roleId);
    }

    @Override
    public List<org.example.beans.Role> getAllRoles() {
        String sql = "SELECT * FROM Role";
        return jdbcTemplate.query(sql, roleRowMapper);
    }

    @Override
    public void addUserRole(long userId, long roleId) {
        String sql = "INSERT INTO UserRole (UserId, RoleId) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, roleId);
    }
}