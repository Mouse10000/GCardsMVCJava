package org.example.services.Implementations;

import org.example.beans.Card;
import org.example.beans.Role;
import org.example.beans.User;
import org.example.beans.UserCard;
import org.example.dao.Interface.CardDAO;
import org.example.dao.Interface.RoleDAO;
import org.example.dao.Interface.UserCardDAO;
import org.example.dao.Interface.UserDAO;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Role.RoleNotFoundException;
import org.example.services.Interface.Exception.User.AuthenticationException;
import org.example.services.Interface.Exception.User.DuplicateUserException;
import org.example.services.Interface.Exception.User.InvalidUserException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

@Service
public class UserServiceImpl implements UserService {
    private final CardDAO cardDAO;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final UserCardDAO userCardDAO;

    public UserServiceImpl(CardDAO cardDAO, UserDAO userDAO, RoleDAO roleDAO, UserCardDAO userCardDAO) {
        this.cardDAO = cardDAO;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userCardDAO = userCardDAO;
    }

    @Override
    public void registerUser(User user)
            throws DuplicateUserException, InvalidUserException, NoSuchAlgorithmException {
        // Проверка на пустые поля
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            throw new InvalidUserException("Username cannot be empty.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserException("Email cannot be empty.");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new InvalidUserException("Password cannot be empty.");
        }

        // Проверка на уникальность имени пользователя и email
        if (userDAO.getUserByUserName(user.getUserName()) != null) {
            throw new DuplicateUserException("User with username "
                    + user.getUserName() + " already exists.");
        }
        /*if (userDAO.getUserByEmail(user.getEmail()) != null) {
            throw new DuplicateUserException("User with email " + user.getEmail() + " already exists.");
        }*/

        // Хэширование пароля
        user.setPasswordHash(hashPassword(user.getPasswordHash()));

        // Добавление пользователя в базу данных
        userDAO.addUser(user);
    }

    @Override
    public User authenticateUser(String username, String password)
            throws AuthenticationException, NoSuchAlgorithmException {
        // Поиск пользователя по имени
        User user = userDAO.getUserByUserName(username);
        if (user == null) {
            throw new AuthenticationException("User with username " + username + " not found.");
        }

        // Проверка пароля
        if (!user.getPasswordHash().equals(hashPassword(password))) {
            throw new AuthenticationException("Invalid password.");
        }

        return user;
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException, InvalidUserException {
        // Проверка существования пользователя
        if (userDAO.getUserById(user.getId()) == null) {
            throw new UserNotFoundException("User with ID " + user.getId() + " not found.");
        }

        // Проверка на пустые поля
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            throw new InvalidUserException("Username cannot be empty.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserException("Email cannot be empty.");
        }

        // Обновление пользователя
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(long userId) throws UserNotFoundException {
        // Проверка существования пользователя
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        // Удаление пользователя
        userDAO.deleteUser(userId);
    }

    @Override
    public User getUserById(long userId) throws UserNotFoundException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        return user;
    }

    @Override
    public User getUserByUserName(String username) throws UserNotFoundException {
        User user = userDAO.getUserByUserName(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        return user;
    }

    @Override
    public List<Card> getUserCards(long userId) throws UserNotFoundException {
        // Проверка существования пользователя
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        // Получение карточек пользователя
        return userDAO.getCardsByUserId(userId);
    }

    @Override
    public void addCardToUser(long userId, long cardId) throws UserNotFoundException, CardNotFoundException {
        // Проверка существования пользователя
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        // Проверка существования карточки
        if (cardDAO.getCardById(cardId) == null) {
            throw new CardNotFoundException("Card with ID " + cardId + " not found.");
        }

        // Добавление карточки пользователю
        UserCard userCard = new UserCard();
        userCard.setUserId(userId);
        userCard.setCardId(cardId);
        userCard.setCountDuplicate(1); // По умолчанию 1 дубликат
        userCardDAO.addUserCard(userCard);
    }

    @Override
    public void removeCardFromUser(long userId, long cardId)
            throws UserNotFoundException, CardNotFoundException {
        // Проверка существования пользователя
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        // Проверка существования карточки
        if (cardDAO.getCardById(cardId) == null) {
            throw new CardNotFoundException("Card with ID " + cardId + " not found.");
        }
        List<UserCard> userCards = userCardDAO.getUserCardsByUserId(userId);
        UserCard result = findObjectByKey(userCards, UserCard::getCardId, cardId);
        if (result.getCountDuplicate() > 0) {
            result.setCountDuplicate(result.getCountDuplicate() - 1);
        }
        else {
            userCardDAO.deleteUserCard(result.getId());
        }
    }

    @Override
    public List<Role> getUserRoles(long userId) throws UserNotFoundException {
        // Проверка существования пользователя
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        // Получение ролей пользователя
        return userDAO.getRolesByUserId(userId);
    }

    @Override
    public void assignRoleToUser(long userId, long roleId)
            throws UserNotFoundException, RoleNotFoundException {
        // Проверка существования пользователя
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        // Проверка существования роли
        if (roleDAO.getRoleById(roleId) == null) {
            throw new RoleNotFoundException("Role with ID " + roleId + " not found.");
        }

        // Назначение роли пользователю
        userDAO.addUserRole(userId, roleId);
    }


    private String hashPassword(String password) throws NoSuchAlgorithmException {

        // Создаем объект MessageDigest с использованием алгоритма SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
        byte[] hash = md.digest(password.getBytes());

        return Base64.getEncoder().encodeToString(hash);
    }
    public static <K, V> V findObjectByKey(List<V> list, Function<V, K> keyExtractor, K key) {
        Map<K, V> map = new HashMap<>();
        for (V item : list) {
            map.put(keyExtractor.apply(item), item);
        }
        return map.get(key);
    }
}