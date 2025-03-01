package org.example.dao.DataSource;

import org.example.beans.Card;
import org.example.dao.Interface.CardDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourceCardDAO implements CardDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCard(Card card) {
        String sql = "INSERT INTO cards (name, date_of_add, description, image, rank, number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, card.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(card.getDateOfAdd()));
            stmt.setString(3, card.getDescription());
            stmt.setBytes(4, card.getImage());
            stmt.setString(5, card.getRank());
            stmt.setInt(6, card.getNumber());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    card.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Card getCardById(Long cardId) {
        String sql = "SELECT * FROM cards WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getLong("id"));
                    card.setName(rs.getString("name"));
                    card.setDateOfAdd(rs.getTimestamp("date_of_add").toLocalDateTime());
                    card.setDescription(rs.getString("description"));
                    card.setImage(rs.getBytes("image"));
                    card.setRank(rs.getString("rank"));
                    card.setNumber(rs.getInt("number"));
                    return card;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateCard(Card card) {
        String sql = "UPDATE cards SET name = ?, date_of_add = ?, description = ?, image = ?, rank = ?, number = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(card.getDateOfAdd()));
            stmt.setString(3, card.getDescription());
            stmt.setBytes(4, card.getImage());
            stmt.setString(5, card.getRank());
            stmt.setInt(6, card.getNumber());
            stmt.setLong(7, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCard(Long cardId) {
        String sql = "DELETE FROM cards WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Card card = new Card();
                card.setId(rs.getLong("id"));
                card.setName(rs.getString("name"));
                card.setDateOfAdd(rs.getTimestamp("date_of_add").toLocalDateTime());
                card.setDescription(rs.getString("description"));
                card.setImage(rs.getBytes("image"));
                card.setRank(rs.getString("rank"));
                card.setNumber(rs.getInt("number"));
                cards.add(card);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cards;
    }
}