package org.example.controllers.oneway;

import org.example.models.Card;
import org.example.services.Interface.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // Просмотр списка всех карточек
    @GetMapping
    public String listCards(Model model) {
        model.addAttribute("cards", cardService.getAllCards());
        return "cards/list";
    }

    // Форма добавления новой карточки
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("card", new Card());
        return "cards/add-form";
    }

    // Обработка добавления карточки
    @PostMapping("/add")
    public String addCard(@ModelAttribute Card card,
                          RedirectAttributes redirectAttributes) throws DuplicateCardException, InvalidCardException {

        cardService.addCard(card);

        redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно добавлена!");
        return "redirect:/cards";
    }

    // Форма редактирования карточки
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Card card = null;
        try {
            card = cardService.getCardById(id);
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (card == null) {
            return "redirect:/cards";
        }
        else {
            model.addAttribute("card", card);
            return"cards/edit-form";
        }
    }

    // Обработка редактирования карточки
    @PostMapping("/edit")
    public String editCard(Card card) {
        try {
            cardService.updateCard(card);
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidCardException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/cards";
    }

    // Форма подтверждения удаления
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        Card card = null;
        try {
            card = cardService.getCardById(id);
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        try {
            cardService.deleteCard(id);
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно удалена!");
        return "redirect:/cards";
    }
}