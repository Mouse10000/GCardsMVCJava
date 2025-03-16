package org.example.services.Interface.Exception.Trade;

public class InvalidTradeStateException extends TradeException {
    public InvalidTradeStateException(String message) {
        super(message);
    }
}
