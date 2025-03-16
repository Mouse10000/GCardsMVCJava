package org.example.dao.Interface;

import org.example.beans.Card;
import org.example.beans.Role;
import org.example.beans.User;
import org.example.beans.UserCard;

import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUserById(long userId);
    void updateUser(User user);
    void deleteUser(long userId);
    List<User> getAllUsers();
    User getUserByUserName(String userName);

    List<Card> getCardsByUserId(long userId);
    void addUserCard(UserCard userCard);
    void deleteUserCard(Long userCardId);
    void updateUserCard(UserCard userCard);

    List<Role> getRolesByUserId(long userId);
    void addUserRole(long userId, long roleId);
    void removeUserRole(long userId, long roleId);
}