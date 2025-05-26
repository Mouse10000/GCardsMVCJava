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

/**
 * Сервис для управления карточками в системе.
 * Предоставляет методы для добавления, обновления, удаления и поиска карточек.
 */
@Service
public class CardService implements CardServiceInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserCardRepository userCardRepository;

    /**
     * Получает все карточки с пагинацией
     * @param pageable параметры пагинации
     * @return страница с карточками
     */
    public Page<Card> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    /**
     * Ищет карточки по имени с учетом регистра
     * @param name имя для поиска
     * @param pageable параметры пагинации
     * @return страница с найденными карточками
     */
    public Page<Card> findByNameContaining(String name, Pageable pageable) {
        return cardRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    /**
     * Ищет карточки по фильтру и поисковому запросу
     * @param filter фильтр для поиска
     * @param query поисковый запрос
     * @param pageable параметры пагинации
     * @return страница с отфильтрованными карточками
     */
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

    /**
     * Добавляет новую карточку в систему
     * @param card карточка для добавления
     * @throws DuplicateCardException если карточка уже существует
     * @throws InvalidCardException если данные карточки некорректны
     */
    @Override
    public void addCard(Card card) throws DuplicateCardException, InvalidCardException {

        if (!CardByNumberAndCardRankIsNull(card.getNumber(), card.getCardRank()))
            throw new DuplicateCardException("Cannot add a duplicate card");
        cardRepository.save(card);
    }

    /**
     * Проверяет существование карточки по номеру и рангу
     * @param number номер карточки
     * @param cardRank ранг карточки
     * @return true если карточка не существует
     */
    @Override
    public Boolean CardByNumberAndCardRankIsNull(int number, String cardRank) {
        Optional<Card> card = cardRepository.findCardByNumberAndCardRank(number,cardRank);
        return card.isEmpty();
    }

    /**
     * Находит карточку по ID
     * @param cardId ID карточки
     * @return найденная карточка
     * @throws CardNotFoundException если карточка не найдена
     */
    public Card findCardById(long cardId) throws CardNotFoundException {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) throw new CardNotFoundException("Card not found");
        return card.get();
    }

    /**
     * Получает список карточек по имени
     * @param name имя карточки
     * @return список найденных карточек
     * @throws CardNotFoundException если карточки не найдены
     */
    @Override
    public List<Card> getCardsByName(String name) throws CardNotFoundException {
        return cardRepository.findAllByName(name);
    }

    /**
     * Обновляет информацию о карточке
     * @param card обновленная карточка
     * @throws CardNotFoundException если карточка не найдена
     * @throws InvalidCardException если данные карточки некорректны
     * @throws DuplicateCardException если обновление создаст дубликат
     */
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

    /**
     * Удаляет карточку по ID
     * @param cardId ID карточки
     * @throws CardNotFoundException если карточка не найдена
     */
    @Override
    public void deleteCard(long cardId) throws CardNotFoundException {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) throw new CardNotFoundException("Card not found");
        cardRepository.deleteById(cardId);
    }

    /**
     * Получает список всех карточек
     * @return список всех карточек
     */
    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    /**
     * Получает карточки для определенной страницы
     * @param page номер страницы
     * @return список карточек на странице
     * @throws CardNotFoundException если карточки не найдены
     */
    @Override
    public List<Card> getCardsOnPage(int page) throws CardNotFoundException {
        Pageable firstPageWithTenElements = PageRequest.of(page - 1, 10);
        Page<Card> cardPage = cardRepository.findAll(firstPageWithTenElements);

        long totalElements = cardPage.getTotalElements(); // Общее количество записей
        int totalPages = cardPage.getTotalPages(); // Общее количество страниц

        return cardPage.getContent();
    }

}