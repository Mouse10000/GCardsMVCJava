package org.example.controllers.server;

import org.example.beans.Trade;
import org.example.models.TradeCreateRequest;
import org.example.models.TradeStateUpdateRequest;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Trade.*;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.TradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/trades")
public class TradeServerController {
    private final TradeService tradeService;

    public TradeServerController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void createTrade(@RequestBody TradeCreateRequest request)
            throws TradeException, CardNotFoundException, UserNotFoundException {
        Trade trade = new Trade();
        trade.setUserSenderId(request.getSenderUserId());
        trade.setUserRecipientId(request.getRecipientUserId());
        trade.setState("pending"); // начальное состояние

        tradeService.createTrade(trade, request.getSenderCardIds(), request.getRecipientCardIds());
    }

    @PatchMapping("/{id}/state")
    public void updateTradeState(@PathVariable Long id, @RequestBody TradeStateUpdateRequest request)
            throws TradeNotFoundException, InvalidTradeStateException {
        tradeService.updateTradeState(id, request.getState());
    }

    @GetMapping("/{id}")
    public Trade getTradeById(@PathVariable Long id) throws TradeNotFoundException {
        return tradeService.getTradeById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Trade> getTradesByUser(@PathVariable Long userId) throws UserNotFoundException {
        return tradeService.getTradesByUser(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteTrade(@PathVariable Long id) throws TradeNotFoundException {
        tradeService.deleteTrade(id);
    }
}