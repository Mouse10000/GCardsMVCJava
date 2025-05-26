package org.example.services;

import org.example.model.Card;
import org.example.model.User;
import org.example.model.UserCard;
import org.example.repository.CardRepository;
import org.example.repository.UserCardRepository;
import org.example.repository.UserRepository;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.UserCardInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Сервис для управления связями между пользователями и карточками.
 * Предоставляет методы для добавления карточек пользователям и управления их коллекциями.
 */
@Service
public class UserCardService implements UserCardInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserCardRepository userCardRepository;

    /**
     * Добавляет карточку пользователю
     * @param userName имя пользователя
     * @param cardId ID карточки
     * @throws CardNotFoundException если карточка не найдена
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void addUserCard(String userName, long cardId) throws CardNotFoundException, UserNotFoundException {
        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty()) throw new CardNotFoundException("Card not found");
        Optional<User> userBase = userRepository.findByUsername(userName);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        UserCard userCard = new UserCard(userBase.get(), cardBase.get());
        userCardRepository.save(userCard);
    }

    /**
     * Добавляет список карточек пользователю
     * @param userName имя пользователя
     * @param cards список карточек
     * @throws CardNotFoundException если карточка не найдена
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void addUserCards(String userName, List<Card> cards) throws CardNotFoundException, UserNotFoundException {
        for (Card card : cards) {
            addUserCard(userName, card.getId());
        }
    }

    @Override
    public List<Card> getCardsByName(String name) throws CardNotFoundException {
        return List.of();
    }

    /**
     * Удаляет карточку у пользователя
     * @param userName имя пользователя
     * @param cardId ID карточки
     * @throws CardNotFoundException если карточка не найдена
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void removeUserCard(String userName, long cardId) throws CardNotFoundException, UserNotFoundException {
        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty()) throw new CardNotFoundException("Card not found");
        Optional<User> userBase = userRepository.findByUsername(userName);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        UserCard userCard = new UserCard(userBase.get(), cardBase.get(), 0);
        userCardRepository.delete(userCard);
    }

    /**
     * Удаляет список карточек у пользователя
     * @param userName имя пользователя
     * @param cards список карточек
     * @throws CardNotFoundException если карточка не найдена
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void removeUserCards(String userName, List<Card> cards) throws CardNotFoundException, UserNotFoundException {
        for (Card card : cards) {
            removeUserCard(userName, card.getId());
        }
    }

    /**
     * Получает все карточки пользователя
     * @param userName имя пользователя
     * @return список карточек пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public List<Card> getAllUserCards(String userName) throws UserNotFoundException {
        Optional<User> userBase = userRepository.findByUsername(userName);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");
        return userCardRepository.findCardsByUser(userBase.get());
    }

    /**
     * Получает карточки пользователя с фильтрацией
     * @param userName имя пользователя
     * @param query поисковый запрос
     * @param rank ранг карточки
     * @param minNumber минимальный номер
     * @param maxNumber максимальный номер
     * @param pageable параметры пагинации
     * @return страница с отфильтрованными карточками
     * @throws UserNotFoundException если пользователь не найден
     */
    public Page<Card> getUserCardsByFilter(String userName, String query, String rank,
                                           Integer minNumber, Integer maxNumber,
                                           Pageable pageable) throws UserNotFoundException {

        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (Objects.equals(rank, "")) rank = null;
        return userCardRepository.findUserCardsByFilter(
                user.get(),
                query,
                rank,
                minNumber,
                maxNumber,
                pageable
        );
    }

    @Override
    public List<Card> getCardsOnPage(int page) throws CardNotFoundException {
        return List.of();
    }

    @Override
    public Card getCardByNumber(int number) {
        return null;
    }
}