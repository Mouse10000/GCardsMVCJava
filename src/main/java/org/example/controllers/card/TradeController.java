package org.example.controllers.card;

import org.example.model.Trade;
import org.example.services.CardService;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Trade.TradeException;
import org.example.services.Interface.Exception.Trade.TradeNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.TradeService;
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

/**
 * Контроллер для управления обменом картами между пользователями.
 * Обрабатывает запросы на создание, просмотр и управление обменами.
 */
@Controller
@RequestMapping("/trades")
@PreAuthorize("hasAuthority('USER')")
public class TradeController {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private CardService cardService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCardService userCardService;

    /**
     * Отображает список всех обменов
     * @param model модель для передачи данных в представление
     * @return имя представления для страницы со списком обменов
     */
    @GetMapping
    public String showTrades(Model model,
                             @AuthenticationPrincipal User user) throws CardNotFoundException {

        List<Trade> tradesSender = null;
        List<Trade> tradesRecipient = null;
        List<Trade> completedTrades = null;
        String username = user.getUsername();
        try {
            tradesSender = tradeService.getTradesByUserSender(username);
            tradesRecipient = tradeService.getTradesByUserRecipient(username);
            completedTrades = tradeService.getTradesCompleted(username);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("yourTrades", tradesSender);
        model.addAttribute("tradesForYour", tradesRecipient);
        model.addAttribute("completedTrades", completedTrades);
        return "trades/list";
    }

    /**
     * Отображает форму создания нового обмена
     * @param model модель для передачи данных в представление
     * @return имя представления для формы создания обмена
     */
    @GetMapping("/init")
    public String initTradeForm(Model model,
                                @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("trade", new Trade());
            model.addAttribute("users",
                    userService.getAllUsersWithoutOne(user.getUsername()));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "trades/choose-user";
    }

    /**
     * Создает новый обмен
     * @param trade данные нового обмена
     * @return перенаправление на список обменов
     */
    @PostMapping("/init")
    public String initTrade(@ModelAttribute Trade trade,
                            @AuthenticationPrincipal User user) {
        Long newTradeId;
        try {
            newTradeId = tradeService.initTrade(
                    user.getUsername(), trade.getUserRecipient().getUsername());

        } catch (UserNotFoundException | TradeException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/trades/form-sender-cards/" + newTradeId;

    }

    @GetMapping("/form-sender-cards/{tradeId}")
    public String formAddCardSender(Model model, @PathVariable Long tradeId,
                              @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("userCards",
                    userCardService.getAllUserCards(user.getUsername()));
            model.addAttribute("userSenderCards",
                    tradeService.getSenderCards(tradeId));
            model.addAttribute("tradeId", tradeId);
        } catch (UserNotFoundException | TradeNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "trades/form-sender-cards";
    }

    @GetMapping("/form-sender-cards/{tradeId}/add/{id}")
    public String addCardSender(@PathVariable Long tradeId, @PathVariable Long id) {
        try {
            tradeService.setSenderCard(tradeId, id);
        } catch (TradeNotFoundException | UserNotFoundException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trades/form-sender-cards/" + tradeId;
    }

    @GetMapping("/form-sender-cards/{tradeId}/delete/{id}")
    public String deleteCardSender(@PathVariable Long tradeId, @PathVariable Long id) {
        try {
            tradeService.deleteSenderCard(tradeId, id);
        } catch (TradeNotFoundException | UserNotFoundException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trades/form-sender-cards/" + tradeId;
    }

    @GetMapping("/form-recipient-cards/{tradeId}")
    public String formAddRecipientCard(Model model, @PathVariable Long tradeId) {
        try {
            org.example.model.User userRecipient = tradeService.getUser(tradeId, "recipient");
            model.addAttribute("userCards",
                    tradeService.getRecipientCardsNotInTrade(tradeId));
            model.addAttribute("userRecipientCards",
                    tradeService.getRecipientCards(tradeId));
            model.addAttribute("tradeId", tradeId);
        } catch (UserNotFoundException | TradeNotFoundException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "trades/form-recipient-cards";
    }

    @GetMapping("/form-recipient-cards/{tradeId}/add/{id}")
    public String addCardRecipient(@PathVariable Long tradeId, @PathVariable Long id) {
        try {
            tradeService.setRecipientCard(tradeId, id);
        } catch (TradeNotFoundException | UserNotFoundException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trades/form-recipient-cards/" + tradeId;
    }

    @GetMapping("/form-recipient-cards/{tradeId}/delete/{id}")
    public String deleteCardRecipient(@PathVariable Long tradeId, @PathVariable Long id) {
        try {
            tradeService.deleteRecipientCard(tradeId, id);
        } catch (TradeNotFoundException | UserNotFoundException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trades/form-recipient-cards/" + tradeId;
    }

    /**
     * Отображает детальную информацию об обмене
     * @param id идентификатор обмена
     * @param model модель для передачи данных в представление
     * @return имя представления для страницы с деталями обмена
     */
    @GetMapping("/details/{id}")
    public String editTrade(Model model, @PathVariable Long id,
                            @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("userSenderCards",
                    tradeService.getSenderCards(id));
            model.addAttribute("userRecipientCards",
                    tradeService.getRecipientCards(id));

            Trade trade = tradeService.getTradeById(id);
            model.addAttribute("tradeId", id);

            model.addAttribute("tradePost", tradeService.tradePosted(id));
            model.addAttribute("tradeCompleted", tradeService.tradeCompleted(id));

            Boolean userIsRecipient = trade.getUserRecipient().getUsername().equals(user.getUsername());
            model.addAttribute("userIsRecipient", userIsRecipient);

        } catch (TradeNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "trades/details";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrade(@PathVariable Long id){
        try {
            tradeService.deleteTrade(id);
        } catch (TradeNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trades";
    }

    @GetMapping("/post-trade/{id}")
    public String postTrade(@PathVariable Long id){
        try {
            tradeService.createTrade(id);
        } catch (UserNotFoundException | TradeException | CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trades";
    }

    @GetMapping("/submit/{id}")
    public String submitTrade(@PathVariable Long id,
                              @AuthenticationPrincipal User user){
        Trade trade = null;
        try {
            trade = tradeService.getTradeById(id);
        } catch (TradeNotFoundException e) {
            throw new RuntimeException(e);
        }
        boolean userIsRecipient = trade.getUserRecipient().getUsername().equals(user.getUsername());
        if(userIsRecipient){
            try {
                tradeService.submitTrade(id);
            } catch (TradeException | UserNotFoundException | CardNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return "redirect:/trades";
    }
    @GetMapping("/cancel/{id}")
    public String cancelTrade(@PathVariable Long id,
                              @AuthenticationPrincipal User user){
        Trade trade = null;
        try {
            trade = tradeService.getTradeById(id);
        } catch (TradeNotFoundException e) {
            throw new RuntimeException(e);
        }
        boolean userIsRecipient = trade.getUserRecipient().getUsername().equals(user.getUsername());
        if(userIsRecipient){
            try {
                tradeService.cancelTrade(id);
            } catch (TradeException | UserNotFoundException | CardNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return "redirect:/trades";
    }

}
