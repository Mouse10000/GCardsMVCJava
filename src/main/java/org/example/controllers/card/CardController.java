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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Контроллер для управления картами.
 * Обрабатывает запросы на создание, просмотр, редактирование и удаление карт.
 */
@Controller
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;

    /**
     * Отображает список всех карт
     * @param model модель для передачи данных в представление
     * @return имя представления для страницы со списком карт
     */
    @GetMapping
    public String showCards(Model model,
                            @RequestParam(required = false) String query,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "12") int size,
                            @RequestParam(defaultValue = "") String sort,
                            @RequestParam(defaultValue = "") String rank,
                            @RequestParam(defaultValue = "0") Integer minNumber,
                            @RequestParam(defaultValue = "10000") Integer maxNumber) {

        CardFilter filter = new CardFilter(rank, minNumber, maxNumber);
        Sort sorting = parseSortParameter(sort);
        Pageable pageable = PageRequest.of(page - 1, size, sorting);

        Page<Card> cardPage = cardService.findByFilterAndSearch(filter, query, pageable);

        UserCardController.setModelAtribute(model, query, page, size, sort, rank, minNumber, maxNumber, cardPage);

        return "cards/index";
    }

    private Sort parseSortParameter(String sort) {
        if (sort.contains(",")) {
            String[] sortParams = sort.split(",");
            return Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        } else {
            return Sort.by(Sort.Direction.ASC, "id");
        }
    }

    /**
     * Отображает форму создания новой карты
     * @param model модель для передачи данных в представление
     * @return имя представления для формы создания карты
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add")
    public String formAddCard(Model model,
                              @RequestParam(required = false) String query,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "12") int size,
                              @RequestParam(defaultValue = "id,asc") String sort,
                              @RequestParam(required = false) String rank,
                              @RequestParam(required = false) Integer minNumber,
                              @RequestParam(required = false) Integer maxNumber) {

        model.addAttribute("card", new Card());
        addPaginationAttributes(model, page, size, sort, query, rank, minNumber, maxNumber);
        return "cards/add-form";
    }

    /**
     * Создает новую карту
     * @param card данные новой карты
     * @return перенаправление на список карт
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String addCard(@ModelAttribute Card card,
                          RedirectAttributes redirectAttributes,
                          @RequestParam(required = false) String query,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "12") int size,
                          @RequestParam(defaultValue = "id,asc") String sort,
                          @RequestParam(required = false) String rank,
                          @RequestParam(required = false) Integer minNumber,
                          @RequestParam(required = false) Integer maxNumber)
            throws DuplicateCardException, InvalidCardException, CardNotFoundException {

        if (!cardService.CardByNumberAndCardRankIsNull(card.getNumber(),card.getCardRank())) {
            redirectAttributes.addFlashAttribute("error", "карточка с таким номером уже существует");
            return "redirect:/cards/add?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        }
        cardService.addCard(card);
        return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
    }

    /**
     * Удаляет карту
     * @param id идентификатор карты
     * @return перенаправление на список карт
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(required = false) String query,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "12") int size,
                             @RequestParam(defaultValue = "id,asc") String sort,
                             @RequestParam(required = false) String rank,
                             @RequestParam(required = false) Integer minNumber,
                             @RequestParam(required = false) Integer maxNumber) {

        try {
            cardService.deleteCard(id);
            return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        } catch (CardNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        }
    }

    /**
     * Отображает форму редактирования карты
     * @param id идентификатор карты
     * @param model модель для передачи данных в представление
     * @return имя представления для формы редактирования карты
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/update/{id}")
    public String updateCardForm(@PathVariable Long id, Model model,
                                 @RequestParam(required = false) String query,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "12") int size,
                                 @RequestParam(defaultValue = "id,asc") String sort,
                                 @RequestParam(required = false) String rank,
                                 @RequestParam(required = false) Integer minNumber,
                                 @RequestParam(required = false) Integer maxNumber) {

        try {
            model.addAttribute("card", cardService.findCardById(id));
            addPaginationAttributes(model, page, size, sort, query, rank, minNumber, maxNumber);
            return "cards/edit-form";
        } catch (CardNotFoundException e) {
            return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        }
    }

    /**
     * Обновляет информацию о карте
     * @param card обновленные данные карты
     * @return перенаправление на детали карты
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update")
    public String updateCard(@ModelAttribute Card card,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(required = false) String query,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "12") int size,
                             @RequestParam(defaultValue = "id,asc") String sort,
                             @RequestParam(required = false) String rank,
                             @RequestParam(required = false) Integer minNumber,
                             @RequestParam(required = false) Integer maxNumber) {

        try {
            cardService.updateCard(card);
            return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        } catch (CardNotFoundException | InvalidCardException | DuplicateCardException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cards/update/" + card.getId() + "?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        }
    }

    /**
     * Отображает детальную информацию о карте
     * @param id идентификатор карты
     * @param model модель для передачи данных в представление
     * @return имя представления для страницы с деталями карты
     */
    @GetMapping("/details/{id}")
    public String showOneCard(@PathVariable Long id, Model model,
                              @RequestParam(required = false) String query,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "12") int size,
                              @RequestParam(defaultValue = "id,asc") String sort,
                              @RequestParam(required = false) String rank,
                              @RequestParam(required = false) Integer minNumber,
                              @RequestParam(required = false) Integer maxNumber) {

        try {
            model.addAttribute("card", cardService.findCardById(id));
            addPaginationAttributes(model, page, size, sort, query, rank, minNumber, maxNumber);
            return "cards/details";
        } catch (CardNotFoundException e) {
            return "redirect:/cards?" + buildQueryParams(page, size, sort, query, rank, minNumber, maxNumber);
        }
    }

    static void addPaginationAttributes(Model model, int page, int size, String sort,
                                         String query, String rank, Integer minNumber, Integer maxNumber) {
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("query", query);
        model.addAttribute("rank", rank);
        model.addAttribute("minNumber", minNumber);
        model.addAttribute("maxNumber", maxNumber);
    }

    static String buildQueryParams(int page, int size, String sort,
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