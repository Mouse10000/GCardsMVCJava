package org.example.dao.Interface;

import org.example.beans.UserCard;

import java.util.List;

public interface UserCardDAO {
    void addUserCard(UserCard userCard);
    UserCard getUserCardById(Long userCardId);
    void updateUserCard(UserCard userCard);
    void deleteUserCard(Long userCardId);
    List<UserCard> getAllUserCards();
    List<UserCard> getUserCardsByUserName(String userName);
}
