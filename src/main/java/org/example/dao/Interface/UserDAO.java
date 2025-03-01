package org.example.dao.Interface;

import org.example.beans.User;

import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUserById(String userId);
    void updateUser(User user);
    void deleteUser(String userId);
    List<User> getAllUsers();
    User getUserByUserName(String userName);
}
