package org.example.services;

import org.example.model.Card;
import org.example.model.dto.CardFilter;
import org.example.repository.CardRepository;
import org.example.repository.UserCardRepository;
import org.example.repository.UserRepository;
import org.example.services.Interface.CardServiceInterface;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService implements CardServiceInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserCardRepository userCardRepository;

    //@Override
    public Page<Card> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    //@Override
    public Page<Card> findByNameContaining(String name, Pageable pageable) {
        return cardRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Card> findByFilterAndSearch(CardFilter filter, String query, Pageable pageable) {
        Specification<Card> spec = Specification.where(null);

        // Добавляем условия поиска (если query не пустой)
        if (query != null && !query.isEmpty()) {
            String searchTerm = "%" + query.toLowerCase() + "%";
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchTerm),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchTerm),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("cardRank")), searchTerm),
                            criteriaBuilder.like(root.get("number").as(String.class), "%" + query + "%")
                    )
            );
        }

        // Добавляем условия фильтрации (если filter не пустой)
        if (filter != null) {
            if (filter.getRank() != null && !filter.getRank().isEmpty()) {
                spec = spec.and((root, query1, cb) ->
                        cb.like(cb.lower(root.get("cardRank")), filter.getRank().toLowerCase()));
            }

            if (filter.getMinNumber() != null) {
                spec = spec.and((root, query1, cb) ->
                        cb.greaterThanOrEqualTo(root.get("number"), filter.getMinNumber()));
            }

            if (filter.getMaxNumber() != null) {
                spec = spec.and((root, query1, cb) ->
                        cb.lessThanOrEqualTo(root.get("number"), filter.getMaxNumber()));
            }
        }

        return cardRepository.findAll(spec, pageable);
    }

    //@Override
    /*public Page<Card> findByFilter(CardFilter filter, Pageable pageable) {
        Specification<Card> spec = Specification.where(null);

        if (filter.getRank() != null && !filter.getRank().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("cardRank")), filter.getRank()));
        }

        if (filter.getMinNumber() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("number"), filter.getMinNumber()));
        }

        if (filter.getMaxNumber() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("number"), filter.getMaxNumber()));
        }

        return cardRepository.findAll(spec, pageable);
    }
    public Page<Card> search(String query, Pageable pageable) {
        return cardRepository.search(query, pageable);
    }*/

    @Override
    public void addCard(Card card) throws DuplicateCardException, InvalidCardException {

        if (!CardByNumberAndCardRankIsNull(card.getNumber(), card.getCardRank()))
            throw new DuplicateCardException("Cannot add a duplicate card");
        cardRepository.save(card);
    }

    @Override
    public Boolean CardByNumberAndCardRankIsNull(int number, String cardRank) {
        Optional<Card> card = cardRepository.findCardByNumberAndCardRank(number,cardRank);
        return card.isEmpty();
    }

    public Card findCardById(long cardId) throws CardNotFoundException {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) throw new CardNotFoundException("Card not found");
        return card.get();
    }

    @Override
    public List<Card> getCardsByName(String name) throws CardNotFoundException {
        return cardRepository.findAllByName(name);
    }

    @Override
    public void updateCard(Card card) throws CardNotFoundException, InvalidCardException, DuplicateCardException {
        Optional<Card> cardBase = cardRepository.findById(card.getId());
        if (cardBase.isEmpty()) throw new CardNotFoundException("Card not found");
        cardRepository.save(card);
        List<Card> cardBase2 = cardRepository.findCardsByNumber(card.getNumber());
        if (cardBase2.size() == 2){
            cardRepository.save(cardBase.get());
            throw new DuplicateCardException("Duplicate card");
        }

    }

    @Override
    public void deleteCard(long cardId) throws CardNotFoundException {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) throw new CardNotFoundException("Card not found");
        cardRepository.deleteById(cardId);
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> getCardsOnPage(int page) throws CardNotFoundException {
        Pageable firstPageWithTenElements = PageRequest.of(page - 1, 10);
        Page<Card> cardPage = cardRepository.findAll(firstPageWithTenElements);

        long totalElements = cardPage.getTotalElements(); // Общее количество записей
        int totalPages = cardPage.getTotalPages(); // Общее количество страниц

        return cardPage.getContent();
    }

}