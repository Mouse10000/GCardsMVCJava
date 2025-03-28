package org.example.dao.Repository;

import org.example.beans.CardRecipient;
import org.example.dao.Interface.CardRecipientDAO;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса CardRecipientDAO для работы с карточками-получателями в базе данных.
 * Предоставляет методы для добавления, получения, удаления и поиска карточек-получателей.
 */
@Component
public class CardRecipientRepository implements CardRecipientDAO {

    private DataSource dataSource;

    /**
     * Устанавливает источник данных (DataSource) для подключения к базе данных.
     *
     * @param dataSource источник данных для подключения к БД
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Добавляет новую карточку-получателя в базу данных.
     *
     * @param cardRecipient объект CardRecipient для добавления
     * @throws RuntimeException если возникает ошибка при работе с базой данных
     */
    @Override
    public void addCardRecipient(CardRecipient cardRecipient) {
        String sql = "INSERT INTO CardRecipient (TradeId, CardId) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, cardRecipient.getTradeId());
            statement.setLong(2, cardRecipient.getCardId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cardRecipient.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении карточки-получателя", e);
        }
    }

    /**
     * Получает карточку-получателя по её идентификатору.
     *
     * @param cardRecipientId идентификатор карточки-получателя
     * @return объект CardRecipient или null, если карточка не найдена
     * @throws RuntimeException если возникает ошибка при работе с базой данных
     */
    @Override
    public CardRecipient getCardRecipientById(int cardRecipientId) {
        String sql = "SELECT * FROM CardRecipient WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardRecipientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCardRecipientFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточки-получателя по ID", e);
        }
        return null;
    }

    /**
     * Удаляет карточку-получателя по её идентификатору.
     *
     * @param cardRecipientId идентификатор карточки-получателя для удаления
     * @throws RuntimeException если возникает ошибка при работе с базой данных
     */
    @Override
    public void deleteCardRecipient(int cardRecipientId) {
        String sql = "DELETE FROM CardRecipient WHERE Id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardRecipientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении карточки-получателя", e);
        }
    }

    /**
     * Получает список всех карточек-получателей из базы данных.
     *
     * @return список объектов CardRecipient
     * @throws RuntimeException если возникает ошибка при работе с базой данных
     */
    @Override
    public List<CardRecipient> getAllCardRecipients() {
        List<CardRecipient> cardRecipients = new ArrayList<>();
        String sql = "SELECT * FROM CardRecipient";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                cardRecipients.add(mapCardRecipientFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех карточек-получателей", e);
        }
        return cardRecipients;
    }

    /**
     * Получает список карточек-получателей по идентификатору обмена.
     *
     * @param tradeId идентификатор обмена
     * @return список объектов CardRecipient, связанных с указанным обменом
     * @throws RuntimeException если возникает ошибка при работе с базой данных
     */
    @Override
    public List<CardRecipient> getCardRecipientsByTradeId(Long tradeId) {
        List<CardRecipient> cardRecipients = new ArrayList<>();
        String sql = "SELECT * FROM CardRecipient WHERE TradeId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tradeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cardRecipients.add(mapCardRecipientFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении карточек-получателей по ID обмена", e);
        }
        return cardRecipients;
    }

    /**
     * Создает объект CardRecipient на основе данных из ResultSet.
     *
     * @param resultSet ResultSet с данными карточки-получателя
     * @return объект CardRecipient
     * @throws SQLException если возникает ошибка при чтении данных из ResultSet
     */
    private CardRecipient mapCardRecipientFromResultSet(ResultSet resultSet) throws SQLException {
        CardRecipient cardRecipient = new CardRecipient();
        cardRecipient.setId(resultSet.getInt("Id"));
        cardRecipient.setTradeId(resultSet.getLong("TradeId"));
        cardRecipient.setCardId(resultSet.getLong("CardId"));
        return cardRecipient;
    }
}