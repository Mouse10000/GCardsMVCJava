package org.example.services.Interface.Exception.Card;

import org.example.services.Interface.Exception.ApplicationException;

public class DuplicateCardException extends ApplicationException {
    public DuplicateCardException(String message) {
        super(message);
    }
}