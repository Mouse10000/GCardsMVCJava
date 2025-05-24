package org.example.controllers.card;

import org.example.model.Card;
import org.example.model.dto.CardFilter;
import org.example.services.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Card.DuplicateCardException;
import org.example.services.Interface.Exception.Card.InvalidCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String showCards(Model model, @ModelAttribute CardFilter filter,
                            @RequestParam(required = false) String name,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "3") int size,
                            @RequestParam(defaultValue = "id,asc") String[] sort) throws CardNotFoundException {

        // Преобразуем параметр сортировки в Sort объект
        Sort sorting = parseSortParameter(sort);
        Pageable pageable = PageRequest.of(page - 1, size, sorting);

        Page<Card> cardPage;

        if (name != null && !name.isEmpty()) {
            cardPage = cardService.findByNameContaining(name, pageable);
        } else if (filter != null && !filter.isEmpty()) {
            cardPage = cardService.findByFilter(filter, pageable);
        } else {
            cardPage = cardService.getAllCards(pageable);
        }
        model.addAttribute("cards", cardPage.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("pageInfo", cardPage); // можно передать весь page объект

        return "cards/index";
    }

    private Sort parseSortParameter(String[] sort) {
        if (sort[1].contains(",")) {
            String[] sortParams = sort[1].split(",");
            return Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        } else {
            // Сортировка по умолчанию
            return Sort.by(Sort.Direction.ASC, "id");
        }
    }

    @GetMapping("/search")
    public String searchCards(@ModelAttribute CardFilter filter,
                              @RequestParam String query,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        Page<Card> cardPage = cardService.search(query, PageRequest.of(page - 1, size));

        model.addAttribute("cards", cardPage.getContent());
        model.addAttribute("query", query);
        model.addAttribute("cards", cardPage.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("pageInfo", cardPage); // можно передать весь page объект

        return "cards/index";
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
    public String updateCardForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("card", cardService.getCardById(id));
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "cards/edit-form";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update")
    public String updateCard(@ModelAttribute Card card, Model model) {
        try {
            cardService.updateCard(card);
        } catch (CardNotFoundException | InvalidCardException e) {
            model.addAttribute("error", "ошибка");
            throw new RuntimeException(e);
        }

        return "redirect:/cards";
    }

    @GetMapping("/details/{id}")
    public String showOneCard(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("card", cardService.getCardById(id));
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "cards/details";
    }
}


