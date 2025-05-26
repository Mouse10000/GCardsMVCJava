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

@Service
public class UserCardService implements UserCardInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserCardRepository userCardRepository;

    @Override
    public void addUserCard(String userName, long cardId) throws CardNotFoundException, UserNotFoundException {
        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty()) throw new CardNotFoundException("Card not found");
        Optional<User> userBase = userRepository.findByUsername(userName);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        UserCard userCard = new UserCard(userBase.get(), cardBase.get());
        userCardRepository.save(userCard);
    }

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

    @Override
    public void removeUserCard(String userName, long cardId) throws CardNotFoundException, UserNotFoundException {
        Optional<Card> cardBase = cardRepository.findById(cardId);
        if (cardBase.isEmpty()) throw new CardNotFoundException("Card not found");
        Optional<User> userBase = userRepository.findByUsername(userName);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");

        UserCard userCard = new UserCard(userBase.get(), cardBase.get(), 0);
        userCardRepository.delete(userCard);
    }

    @Override
    public void removeUserCards(String userName, List<Card> cards) throws CardNotFoundException, UserNotFoundException {
        for (Card card : cards) {
            removeUserCard(userName, card.getId());
        }
    }

    @Override
    public List<Card> getAllUserCards(String userName) throws UserNotFoundException {
        Optional<User> userBase = userRepository.findByUsername(userName);
        if (userBase.isEmpty()) throw new UserNotFoundException("User not found");
        return userCardRepository.findCardsByUser(userBase.get());
    }

    public Page<Card> getUserCardsByFilter(String userName, String query, String rank,
                                           Integer minNumber, Integer maxNumber,
                                           Pageable pageable) throws UserNotFoundException {

        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        Boolean q = (query == null ||
                "КЙЦУ".contains(query)
                && (rank == null || "R".contains(rank))
                && (minNumber == null || 45 >= minNumber)
                && (maxNumber == null || 45 <= maxNumber));
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