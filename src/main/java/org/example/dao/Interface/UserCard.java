package org.example.dao.Interface;

import java.util.List;

public interface UserCard {
    void addUserCard(org.example.beans.UserCard userCard);
    org.example.beans.UserCard getUserCardById(Long userCardId);
    void updateUserCard(org.example.beans.UserCard userCard);
    void deleteUserCard(Long userCardId);
    List<org.example.beans.UserCard> getAllUserCards();
    List<org.example.beans.UserCard> getUserCardsByUserId(Long userId);
}