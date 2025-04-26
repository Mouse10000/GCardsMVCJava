package org.example.services.Interface.Exception.Card;

import org.example.services.Interface.Exception.ApplicationException;

public class CardNotFoundException extends ApplicationException {
    public CardNotFoundException(String message) {
        super(message);
    }
}