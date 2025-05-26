package org.example.services.Interface.Exception.Card;

import org.example.services.Interface.Exception.ApplicationException;

public class InvalidCardException extends ApplicationException {
    public InvalidCardException(String message) {
        super(message);
    }
}