package org.example.services.Interface.Exception.User;

import org.example.services.Interface.Exception.ApplicationException;

public class InvalidUserException extends ApplicationException {
    public InvalidUserException(String message) {
        super(message);
    }
}
