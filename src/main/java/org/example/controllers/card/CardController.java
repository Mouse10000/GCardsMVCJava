package org.example.controllers.card;

import org.example.model.Card;
import org.example.services.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @GetMapping
    public String showCards(Model model,
                           //@RequestParam String name,
                           @RequestParam(defaultValue = "1") int page) throws CardNotFoundException {
        //List<Card> cards = cardService.getCardsOnPage(page);
        List<Card> cards = cardService.getAllCards();
        model.addAttribute("cards", cards);
        return "cards/list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add")
    public String formAddCard(Model model) {
        model.addAttribute("card", new Card());
        return "cards/add-form";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String addCard(@ModelAttribute Card card, Model model) throws DuplicateCardException, InvalidCardException {
        if (cardService.getCardByNumber(card.getNumber()) != null) {
            model.addAttribute("error", "карточка с таким номером уже существует");
            return "cards/add-form";
        }
        cardService.addCard(card);
        return "redirect:/cards";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id) {
        try {
            cardService.deleteCard(id);
            return "redirect:/cards";
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/update/{id}")
    public String updateCardForm(@PathVariable Long id, Model model){
        try {
            model.addAttribute("card", cardService.getCardById(id));
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "cards/edit-form";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update")
    public String updateCard(@ModelAttribute Card card, Model model){
        try {
            cardService.updateCard(card);
        } catch (CardNotFoundException | InvalidCardException e) {
            model.addAttribute("error", "ошибка");
            throw new RuntimeException(e);
        }

        return "redirect:/cards";
    }
}


