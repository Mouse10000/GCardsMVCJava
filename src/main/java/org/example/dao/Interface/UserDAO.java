package org.example.dao.Interface;

import org.example.beans.Card;
import org.example.beans.Role;
import org.example.beans.User;

import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUserById(long userId);
    void updateUser(User user);
    void deleteUser(long userId);
    List<User> getAllUsers();
    User getUserByUserName(String userName);
    List<Card> getCardsByUserId(long userId);
    List<Role> getRolesByUserId(long userId);
}