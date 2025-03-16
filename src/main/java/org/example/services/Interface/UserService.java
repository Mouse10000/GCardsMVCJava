package org.example.services.Interface;

import org.example.beans.Card;
import org.example.beans.Role;
import org.example.beans.User;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Role.RoleNotFoundException;
import org.example.services.Interface.Exception.User.AuthenticationException;
import org.example.services.Interface.Exception.User.DuplicateUserException;
import org.example.services.Interface.Exception.User.InvalidUserException;
import org.example.services.Interface.Exception.User.UserNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface UserService {
    void registerUser(User user) throws DuplicateUserException, InvalidUserException, NoSuchAlgorithmException;
    User authenticateUser(String username, String password) throws AuthenticationException, NoSuchAlgorithmException;
    void updateUser(User user) throws UserNotFoundException, InvalidUserException;
    void deleteUser(long userId) throws UserNotFoundException;
    User getUserById(long userId) throws UserNotFoundException;
    User getUserByUserName(String username) throws UserNotFoundException;

    List<Card> getUserCards(long userId) throws UserNotFoundException;
    void addCardToUser(long userId, long cardId) throws UserNotFoundException, CardNotFoundException, CardNotFoundException;

    void removeCardFromUser(long userId, long cardId) throws UserNotFoundException, CardNotFoundException;

    List<Role> getUserRoles(long userId) throws UserNotFoundException;

    void assignRoleToUser(long userId, long roleId) throws UserNotFoundException, RoleNotFoundException, RoleNotFoundException;
}
