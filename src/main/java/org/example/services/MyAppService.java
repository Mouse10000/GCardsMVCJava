package org.example.services;

import org.example.dao.AuthorDAO;
import org.example.beans.*;

import java.sql.SQLException;
import java.util.List;

import org.example.dao.Interface.*;

public class MyAppService {

    private AuthorDAO authorDAO;
    private CardDAO cardDAO;
    private CardRecipientDAO cardRecipientDAO;
    private CardSenderDAO cardSenderDAO;
    private RoleDAO roleDAO;
    private TradeDAO tradeDAO;
    private UserCardDAO userCardDAO;
    private UserDAO userDAO;
    private UserRoleDAO userRoleDAO;

    // Setter methods for DAOs
    public void setAuthorDAO(AuthorDAO authorDAO) { this.authorDAO = authorDAO; }
    public void setCardDAO(CardDAO cardDAO) { this.cardDAO = cardDAO; }
    public void setCardRecipientDAO(CardRecipientDAO cardRecipientDAO) { this.cardRecipientDAO = cardRecipientDAO; }
    public void setCardSenderDAO(CardSenderDAO cardSenderDAO) { this.cardSenderDAO = cardSenderDAO; }
    public void setRoleDAO(RoleDAO roleDAO) { this.roleDAO = roleDAO; }
    public void setTradeDAO(TradeDAO tradeDAO) { this.tradeDAO = tradeDAO; }
    public void setUserCardDAO(UserCardDAO userCardDAO) { this.userCardDAO = userCardDAO; }
    public void setUserDAO(UserDAO userDAO) { this.userDAO = userDAO; }
    public void setUserRoleDAO(UserRoleDAO userRoleDAO) { this.userRoleDAO = userRoleDAO; }

    // Author Service Methods
    public void addAuthor(Author author) throws SQLException {
        authorDAO.addAuthor(author);
    }
    public Author getAuthorById(int id) throws SQLException {
        return authorDAO.getAuthorById(id);
    }
    public void updateAuthor(Author author) throws SQLException {
        authorDAO.updateAuthor(author);
    }
    public void deleteAuthor(int id) throws SQLException {
        authorDAO.deleteAuthor(id);
    }
    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.getAllAuthors();
    }

    // Card Service Methods
    public void addCard(Card card) throws SQLException {
        cardDAO.addCard(card);
    }
    public Card getCardById(Long cardId) throws SQLException {
        return cardDAO.getCardById(cardId);
    }
    public void updateCard(Card card) throws SQLException {
        cardDAO.updateCard(card);
    }
    public void deleteCard(Long cardId) throws SQLException {
        cardDAO.deleteCard(cardId);
    }
    public List<Card> getAllCards() throws SQLException {
        return cardDAO.getAllCards();
    }

    // CardRecipient Service Methods
    public void addCardRecipient(CardRecipient cardRecipient) throws SQLException {
        cardRecipientDAO.addCardRecipient(cardRecipient);
    }
    public CardRecipient getCardRecipientById(int cardRecipientId) throws SQLException {
        return cardRecipientDAO.getCardRecipientById(cardRecipientId);
    }
    public void updateCardRecipient(CardRecipient cardRecipient) throws SQLException {
        cardRecipientDAO.updateCardRecipient(cardRecipient);
    }
    public void deleteCardRecipient(int cardRecipientId) throws SQLException {
        cardRecipientDAO.deleteCardRecipient(cardRecipientId);
    }
    public List<CardRecipient> getAllCardRecipients() throws SQLException {
        return cardRecipientDAO.getAllCardRecipients();
    }
    public List<CardRecipient> getCardRecipientsByTradeId(Long tradeId) throws SQLException {
        return cardRecipientDAO.getCardRecipientsByTradeId(tradeId);
    }

    // CardSender Service Methods
    public void addCardSender(CardSender cardSender) throws SQLException {
        cardSenderDAO.addCardSender(cardSender);
    }
    public CardSender getCardSenderById(int cardSenderId) throws SQLException {
        return cardSenderDAO.getCardSenderById(cardSenderId);
    }
    public void updateCardSender(CardSender cardSender) throws SQLException {
        cardSenderDAO.updateCardSender(cardSender);
    }
    public void deleteCardSender(int cardSenderId) throws SQLException {
        cardSenderDAO.deleteCardSender(cardSenderId);
    }
    public List<CardSender> getAllCardSenders() throws SQLException {
        return cardSenderDAO.getAllCardSenders();
    }
    public List<CardSender> getCardSendersByTradeId(Long tradeId) throws SQLException {
        return cardSenderDAO.getCardSendersByTradeId(tradeId);
    }

    // Role Service Methods
    public void addRole(Role role) throws SQLException {
        roleDAO.addRole(role);
    }
    public Role getRoleById(String roleId) throws SQLException {
        return roleDAO.getRoleById(roleId);
    }
    public void updateRole(Role role) throws SQLException {
        roleDAO.updateRole(role);
    }
    public void deleteRole(String roleId) throws SQLException {
        roleDAO.deleteRole(roleId);
    }
    public List<Role> getAllRoles() throws SQLException {
        return roleDAO.getAllRoles();
    }

    // Trade Service Methods
    public void addTrade(Trade trade) throws SQLException {
        tradeDAO.addTrade(trade);
    }
    public Trade getTradeById(Long tradeId) throws SQLException {
        return tradeDAO.getTradeById(tradeId);
    }
    public void updateTrade(Trade trade) throws SQLException {
        tradeDAO.updateTrade(trade);
    }
    public void deleteTrade(Long tradeId) throws SQLException {
        tradeDAO.deleteTrade(tradeId);
    }
    public List<Trade> getAllTrades() throws SQLException {
        return tradeDAO.getAllTrades();
    }
    public List<Trade> getTradesByUserSender(String userSender) throws SQLException {
        return tradeDAO.getTradesByUserSender(userSender);
    }
    public List<Trade> getTradesByUserRecipient(String userRecipient) throws SQLException {
        return tradeDAO.getTradesByUserRecipient(userRecipient);
    }

    // UserCard Service Methods
    public void addUserCard(UserCard userCard) throws SQLException {
        userCardDAO.addUserCard(userCard);
    }
    public UserCard getUserCardById(Long userCardId) throws SQLException {
        return userCardDAO.getUserCardById(userCardId);
    }
    public void updateUserCard(UserCard userCard) throws SQLException {
        userCardDAO.updateUserCard(userCard);
    }
    public void deleteUserCard(Long userCardId) throws SQLException {
        userCardDAO.deleteUserCard(userCardId);
    }
    public List<UserCard> getAllUserCards() throws SQLException {
        return userCardDAO.getAllUserCards();
    }
    public List<UserCard> getUserCardsByUserName(String userName) throws SQLException {
        return userCardDAO.getUserCardsByUserName(userName);
    }

    // User Service Methods
    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }
    public User getUserById(String userId) throws SQLException {
        return userDAO.getUserById(userId);
    }
    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }
    public void deleteUser(String userId) throws SQLException {
        userDAO.deleteUser(userId);
    }
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
    public User getUserByUserName(String userName) throws SQLException {
        return userDAO.getUserByUserName(userName);
    }

    // UserRole Service Methods
    public void addUserRole(UserRole userRole) throws SQLException {
        userRoleDAO.addUserRole(userRole);
    }
    public UserRole getUserRoleById(String userId, String roleId) throws SQLException {
        return userRoleDAO.getUserRoleById(userId, roleId);
    }
    public void updateUserRole(UserRole userRole) throws SQLException {
        userRoleDAO.updateUserRole(userRole);
    }
    public void deleteUserRole(String userId, String roleId) throws SQLException {
        userRoleDAO.deleteUserRole(userId, roleId);
    }
    public List<UserRole> getAllUserRoles() throws SQLException {
        return userRoleDAO.getAllUserRoles();
    }
    public List<UserRole> getUserRolesByUserId(String userId) throws SQLException {
        return userRoleDAO.getUserRolesByUserId(userId);
    }
}