package org.example.controllers.card;

import org.example.model.Card;
import org.example.services.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.UserCardService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user-cards")
@PreAuthorize("hasAuthority('USER')")
public class UserCardController {
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;

    @GetMapping
    public String showUserCards(Model model,
                                @AuthenticationPrincipal User user){
        List<Card> cards = null;
        model.addAttribute("userName", user.getUsername());
        try {
            cards = userCardService.getAllUserCards(user.getUsername());
            model.addAttribute("cards", cards);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "user-cards/list";
    }

    @GetMapping("/add/{id}")
    public String addUserCard(Model model, @PathVariable Long id,
                              @AuthenticationPrincipal User user) {
        try {
            userCardService.addUserCard(user.getUsername(), id);
            model.addAttribute("successMessage", "карточка успешно добавлена");
        } catch (CardNotFoundException | UserNotFoundException e) {
            model.addAttribute("successMessage", "карточка не добавлена");
            throw new RuntimeException(e);
        }

        return "redirect:/cards";
    }


    @GetMapping("/delete/{id}")
    public String deleteCard(Model model, @PathVariable Long id,
                             @AuthenticationPrincipal User user) {
        try {
            userCardService.removeUserCard(user.getUsername(), id);
            model.addAttribute("successMessage", "карточка успешно удалена");
        } catch (CardNotFoundException | UserNotFoundException e) {
            model.addAttribute("successMessage", "карточка не удалена");
            throw new RuntimeException(e);
        }

        return "redirect:/user-cards";
    }
/*
    @GetMapping("/update/{id}")
    public String updateCardForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("card", cardService.getCardById(id));
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "cards/edit-form";
    }

    @PostMapping("/update")
    public String updateCard(@ModelAttribute Card card, Model model) {
        try {
            cardService.updateCard(card);
        } catch (CardNotFoundException | InvalidCardException e) {
            model.addAttribute("error", "ошибка");
            throw new RuntimeException(e);
        }

        return "redirect:/cards";
    }*/
}
