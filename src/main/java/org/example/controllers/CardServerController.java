package org.example.controllers;

import org.example.beans.Card;
import org.example.dao.Repository.CardRepository;
import org.example.models.CardForm;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController

@RequestMapping("/api/cards")
public class CardServerController {
    private final CardRepository cardRepository;

    public CardServerController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    //
    // Просмотр списка всех карточек
    @GetMapping
    public List<Card> listCards() {
        return cardRepository.getAllCards();
    }

    //
//    // Форма добавления новой карточки
//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("cardForm", new CardForm());
//        return "cards/add-form";
//    }
//
    // Обработка добавления карточки
    @PostMapping("/{id}")
    @ResponseStatus(CREATED)
    public void addCard(@PathVariable Long id,
                        @RequestBody CardForm cardForm) {
        Card card = new Card();
        card.setName(cardForm.getName());
        card.setDescription(cardForm.getDescription());
        card.setRank(cardForm.getRank());
        card.setNumber(cardForm.getNumber());
        card.setDateOfAdd(LocalDateTime.now());
        card.setImage(new byte[0]); // Заглушка для изображения
        cardRepository.addCard(card);
    }

//    // Форма редактирования карточки
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {
//        Card card = cardRepository.getCardById(id);
//        if (card == null) {
//            return "redirect:/cards";
//        }
//
//        CardForm cardForm = new CardForm();
//        cardForm.setId(card.getId());
//        cardForm.setName(card.getName());
//        cardForm.setDescription(card.getDescription());
//        cardForm.setRank(card.getRank());
//        cardForm.setNumber(card.getNumber());
//
//        model.addAttribute("cardForm", cardForm);
//        return "cards/edit-form";
//    }

    // Обработка редактирования карточки
    @PutMapping("/{id}")
    public void editCard(@PathVariable Long id,
                         @RequestBody CardForm cardForm) {
        Card card = cardRepository.getCardById(cardForm.getId());
        if (card != null) {
            card.setName(cardForm.getName());
            card.setDescription(cardForm.getDescription());
            card.setRank(cardForm.getRank());
            card.setNumber(cardForm.getNumber());

            cardRepository.updateCard(card);
        }
//        return "redirect:/cards";
    }

//    // Форма подтверждения удаления
//    @GetMapping("/delete/{id}")
//    public String showDeleteForm(@PathVariable Long id, Model model) {
//        Card card = cardRepository.getCardById(id);
//        if (card == null) {
//            return "redirect:/cards";
//        }
//        model.addAttribute("card", card);
//        return "cards/delete-confirm";
//    }

    // Обработка удаления карточки
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCard(@PathVariable Long id) {
        cardRepository.deleteCard(id);
    }
}