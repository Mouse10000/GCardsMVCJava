package org.example.dao.Interface;

import org.example.beans.Card;
import org.example.beans.Role;
import org.example.beans.UserCard;

import java.util.List;

public interface User {
    void addUser(org.example.beans.User user);
    org.example.beans.User getUserById(long userId);
    void updateUser(org.example.beans.User user);
    void deleteUser(long userId);
    List<org.example.beans.User> getAllUsers();
    org.example.beans.User getUserByUserName(String userName);

    List<Card> getCardsByUserId(long userId);
    void addUserCard(UserCard userCard);
    void deleteUserCard(Long userCardId);
    void updateUserCard(UserCard userCard);

    List<Role> getRolesByUserId(long userId);
    void addUserRole(long userId, long roleId);
    void removeUserRole(long userId, long roleId);
}