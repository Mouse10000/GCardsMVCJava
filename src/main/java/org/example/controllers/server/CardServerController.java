package org.example.controllers.server;

import org.example.models.Card;
import org.example.services.Interface.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/cards")
public class CardServerController {
    private final CardService cardService;

    public CardServerController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<Card> listCards() {
        return cardService.getAllCards();
    }

    // Обработка добавления карточки
    @PostMapping
    @ResponseStatus(CREATED)
    public void addCard(@RequestBody Card card) throws DuplicateCardException, InvalidCardException {
        cardService.addCard(card);
    }

    // Обработка редактирования карточки
    @PutMapping("/{id}")
    public void editCard(@PathVariable("id") Long id,
                         @RequestBody Card card) throws CardNotFoundException, InvalidCardException {
        cardService.updateCard(card);
    }

    // Обработка удаления карточки
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCard(@PathVariable("id") Long id) throws CardNotFoundException {
        cardService.deleteCard(id);
    }
}