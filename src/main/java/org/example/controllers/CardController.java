package org.example.controllers;

import org.example.beans.Card;
import org.example.dao.Repository.CardRepository;
import org.example.models.CardForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    // Просмотр списка всех карточек
    @GetMapping
    public String listCards(Model model) {
        model.addAttribute("cards", cardRepository.getAllCards());
        return "cards/list";
    }

    // Форма добавления новой карточки
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cardForm", new CardForm());
        return "cards/add-form";
    }

    // Обработка добавления карточки
    @PostMapping("/add")
    public String addCard(@ModelAttribute CardForm cardForm,
                          RedirectAttributes redirectAttributes) {
        Card card = new Card();
        card.setName(cardForm.getName());
        card.setDescription(cardForm.getDescription());
        card.setRank(cardForm.getRank());
        card.setNumber(cardForm.getNumber());
        card.setDateOfAdd(LocalDateTime.now());
        card.setImage(new byte[0]); // Заглушка для изображения

        cardRepository.addCard(card);

        redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно добавлена!");
        return "redirect:/cards";
    }

    // Форма редактирования карточки
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Card card = cardRepository.getCardById(id);
        if (card == null) {
            return "redirect:/cards";
        }

        CardForm cardForm = new CardForm();
        cardForm.setId(card.getId());
        cardForm.setName(card.getName());
        cardForm.setDescription(card.getDescription());
        cardForm.setRank(card.getRank());
        cardForm.setNumber(card.getNumber());

        model.addAttribute("cardForm", cardForm);
        return "cards/edit-form";
    }

    // Обработка редактирования карточки
    @PostMapping("/edit")
    public String editCard(@ModelAttribute CardForm cardForm,
                           RedirectAttributes redirectAttributes) {
        Card card = cardRepository.getCardById(cardForm.getId());
        if (card != null) {
            card.setName(cardForm.getName());
            card.setDescription(cardForm.getDescription());
            card.setRank(cardForm.getRank());
            card.setNumber(cardForm.getNumber());

            cardRepository.updateCard(card);
            redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно обновлена!");
        }
        return "redirect:/cards";
    }

    // Форма подтверждения удаления
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        Card card = cardRepository.getCardById(id);
        if (card == null) {
            return "redirect:/cards";
        }
        model.addAttribute("card", card);
        return "cards/delete-confirm";
    }

    // Обработка удаления карточки
    @PostMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        cardRepository.deleteCard(id);
        redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно удалена!");
        return "redirect:/cards";
    }
}