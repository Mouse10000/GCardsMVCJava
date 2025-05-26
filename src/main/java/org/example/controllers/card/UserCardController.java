package org.example.controllers.card;

import org.example.model.Card;
import org.example.services.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.UserCardService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
                                @AuthenticationPrincipal User user,
                                @RequestParam(required = false) String query,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "4") int size,
                                @RequestParam(defaultValue = "id,asc") String sort,
                                @RequestParam(required = false) String rank,
                                @RequestParam(required = false) Integer minNumber,
                                @RequestParam(required = false) Integer maxNumber) {

        try {
            Sort sorting = parseSortParameter(sort);
            Pageable pageable = PageRequest.of(page - 1, size, sorting);

            Page<Card> cardPage = userCardService.getUserCardsByFilter(
                    user.getUsername(),
                    query,
                    rank,
                    minNumber,
                    maxNumber,
                    pageable
            );

            model.addAttribute("userName", user.getUsername());
            setModelAtribute(model, query, page, size, sort, rank, minNumber, maxNumber, cardPage);

        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "user-cards/index";
    }

    static void setModelAtribute(Model model, @RequestParam(required = false) String query, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "12") int size, @RequestParam(defaultValue = "id,asc") String sort, @RequestParam(required = false) String rank, @RequestParam(required = false) Integer minNumber, @RequestParam(required = false) Integer maxNumber, Page<Card> cardPage) {
        model.addAttribute("cards", cardPage.getContent());
        model.addAttribute("query", query);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cardPage.getTotalPages());
        model.addAttribute("totalItems", cardPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("rank", rank);
        model.addAttribute("minNumber", minNumber);
        model.addAttribute("maxNumber", maxNumber);
    }

    @GetMapping("/add/{id}")
    public String addUserCard(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(required = false) String query,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "4") int size,
                              @RequestParam(defaultValue = "id,asc") String sort,
                              @RequestParam(required = false) String rank,
                              @RequestParam(required = false) Integer minNumber,
                              @RequestParam(required = false) Integer maxNumber) {

        try {
            userCardService.addUserCard(user.getUsername(), id);
            redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно добавлена");
        } catch (CardNotFoundException | UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении карточки");
        }

        return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(required = false) String query,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "4") int size,
                             @RequestParam(defaultValue = "id,asc") String sort,
                             @RequestParam(required = false) String rank,
                             @RequestParam(required = false) Integer minNumber,
                             @RequestParam(required = false) Integer maxNumber) {

        try {
            userCardService.removeUserCard(user.getUsername(), id);
            redirectAttributes.addFlashAttribute("successMessage", "Карточка успешно удалена");
        } catch (CardNotFoundException | UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении карточки");
        }

        return "redirect:/user-cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
    }

    private Sort parseSortParameter(String sort) {
        if (sort.contains(",")) {
            String[] sortParams = sort.split(",");
            return Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        } else {
            return Sort.by(Sort.Direction.ASC, "id");
        }
    }

    private String buildQueryParams(int page, int size, String sort,
                                    String query, String rank, Integer minNumber, Integer maxNumber) {
        StringBuilder params = new StringBuilder();
        params.append("page=").append(page)
                .append("&size=").append(size)
                .append("&sort=").append(sort);

        if (query != null && !query.isEmpty()) {
            params.append("&query=").append(URLEncoder.encode(query, StandardCharsets.UTF_8));
        }
        if (rank != null && !rank.isEmpty()) {
            params.append("&rank=").append(rank);
        }
        if (minNumber != null) {
            params.append("&minNumber=").append(minNumber);
        }
        if (maxNumber != null) {
            params.append("&maxNumber=").append(maxNumber);
        }

        return params.toString();
    }
}