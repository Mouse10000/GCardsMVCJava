package org.example.dao.repository;

import org.example.beans.*;
import org.example.dao.Interface.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepository implements User {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<org.example.beans.User> userRowMapper = (rs, rowNum) -> {
        org.example.beans.User user = new org.example.beans.User();
        user.setId(rs.getLong("Id"));
        user.setUserName(rs.getString("UserName"));
        user.setEmail(rs.getString("Email"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        return user;
    };

    private final RowMapper<Card> cardRowMapper = (rs, rowNum) -> {
        Card card = new Card();
        card.setId(rs.getLong("Id"));
        card.setName(rs.getString("Name"));
        card.setDateOfAdd(rs.getTimestamp("DateOfAdd").toLocalDateTime());
        card.setDescription(rs.getString("Description"));
        card.setImage(rs.getBytes("Image"));
        card.setRank(rs.getString("CardRank"));
        card.setNumber(rs.getInt("Number"));
        return card;
    };

    private final RowMapper<Role> roleRowMapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(rs.getLong("Id"));
        role.setName(rs.getString("Name"));
        return role;
    };

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(org.example.beans.User user) {
        String sql = "INSERT INTO User (Id, UserName, Email, PasswordHash) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPasswordHash());
    }

    @Override
    public org.example.beans.User getUserById(long userId) {
        String sql = "SELECT * FROM User WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, userId);
    }

    @Override
    public void updateUser(org.example.beans.User user) {
        String sql = "UPDATE User SET UserName = ?, Email = ?, PasswordHash = ? WHERE Id = ?";
        jdbcTemplate.update(sql,
                user.getUserName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getId());
    }

    @Override
    public void deleteUser(long userId) {
        String sql = "DELETE FROM User WHERE Id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public List<org.example.beans.User> getAllUsers() {
        String sql = "SELECT * FROM User";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public org.example.beans.User getUserByUserName(String userName) {
        String sql = "SELECT * FROM User WHERE UserName = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, userName);
    }

    @Override
    public List<Card> getCardsByUserId(long userId) {
        String sql = "SELECT c.* FROM Card c JOIN UserCard uc ON c.Id = uc.CardId WHERE uc.UserId = ?";
        return jdbcTemplate.query(sql, cardRowMapper, userId);
    }

    @Override
    public List<Role> getRolesByUserId(long userId) {
        String sql = "SELECT r.* FROM Role r JOIN UserRole ur ON r.Id = ur.RoleId WHERE ur.UserId = ?";
        return jdbcTemplate.query(sql, roleRowMapper, userId);
    }

    @Override
    public void addUserRole(long userId, long roleId) {
        String sql = "INSERT INTO UserRole (UserId, RoleId) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, roleId);
    }

    @Override
    public void removeUserRole(long userId, long roleId) {
        String sql = "DELETE FROM UserRole WHERE UserId = ? AND RoleId = ?";
        jdbcTemplate.update(sql, userId, roleId);
    }

    @Override
    public void addUserCard(UserCard userCard) {
        String sql = "INSERT INTO UserCard (UserId, CardId, CountDuplicate) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userCard.getUserId());
            ps.setLong(2, userCard.getCardId());
            ps.setInt(3, userCard.getCountDuplicate());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            userCard.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void updateUserCard(UserCard userCard) {
        String sql = "UPDATE UserCard SET UserId = ?, CardId = ?, CountDuplicate = ? WHERE Id = ?";
        jdbcTemplate.update(sql,
                userCard.getUserId(),
                userCard.getCardId(),
                userCard.getCountDuplicate(),
                userCard.getId());
    }

    @Override
    public void deleteUserCard(Long userCardId) {
        String sql = "DELETE FROM UserCard WHERE Id = ?";
        jdbcTemplate.update(sql, userCardId);
    }
}