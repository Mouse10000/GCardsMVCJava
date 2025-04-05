package org.example.dao.Repository;

import org.example.beans.UserCard;
import org.example.dao.Interface.UserCardDAO;
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
public class UserCardRepository implements UserCardDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<UserCard> userCardRowMapper = (rs, rowNum) -> {
        UserCard userCard = new UserCard();
        userCard.setId(rs.getLong("Id"));
        userCard.setUserId(rs.getLong("UserId"));
        userCard.setCardId(rs.getLong("CardId"));
        userCard.setCountDuplicate(rs.getInt("CountDuplicate"));
        return userCard;
    };

    @Autowired
    public UserCardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public UserCard getUserCardById(Long userCardId) {
        String sql = "SELECT * FROM UserCard WHERE Id = ?";
        return jdbcTemplate.queryForObject(sql, userCardRowMapper, userCardId);
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

    @Override
    public List<UserCard> getAllUserCards() {
        String sql = "SELECT * FROM UserCard";
        return jdbcTemplate.query(sql, userCardRowMapper);
    }

    @Override
    public List<UserCard> getUserCardsByUserId(Long userId) {
        String sql = "SELECT * FROM UserCard WHERE UserId = ?";
        return jdbcTemplate.query(sql, userCardRowMapper, userId);
    }
}