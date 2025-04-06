package org.example.dao.Repository;

import org.example.beans.Card;
import org.example.config.DatabaseConfig;
import org.example.dao.Interface.CardDAO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CardRepository implements CardDAO {

    private DataSource dataSource;
    // Конструктор с внедрением зависимости
    public CardRepository() {
        this.dataSource = DatabaseConfig.createDataSource();
    }

    // Альтернативный конструктор для тестирования с mock DataSource
    public CardRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCard(Card card) {
        String sql = "INSERT INTO Card (Name, DateOfAdd, Description, Image, CardRank, Number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, card.getName());
            statement.setTimestamp(2, Timestamp.valueOf(card.getDateOfAdd()));
            statement.setString(3, card.getDescription());
            statement.setBytes(4, card.getImage());
            statement.setString(5, card.getRank());
            statement.setInt(6, card.getNumber());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    card.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении карточки", e);
        }
    }

    @Override
    public Card getCardById(long cardId) {
        String sql = "SELECT * FROM Card WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, cardId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCardFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточки по ID", e);
        }
        return null;
    }

    @Override
    public List<Card> getCardByName(String name) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM Card WHERE Name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cards.add(mapCardFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточек по названию", e);
        }
        return cards;
    }

    @Override
    public void updateCard(Card card) {
        String sql = "UPDATE Card SET Name = ?, DateOfAdd = ?, Description = ?, Image = ?, CardRank = ?, Number = ? WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, card.getName());
            statement.setTimestamp(2, Timestamp.valueOf(card.getDateOfAdd()));
            statement.setString(3, card.getDescription());
            statement.setBytes(4, card.getImage());
            statement.setString(5, card.getRank());
            statement.setInt(6, card.getNumber());
            statement.setLong(7, card.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении карточки", e);
        }
    }

    @Override
    public void deleteCard(long cardId) {
        String sql = "DELETE FROM Card WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, cardId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении карточки", e);
        }
    }

    @Override
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM Card";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                cards.add(mapCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех карточек", e);
        }
        return cards;
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
}