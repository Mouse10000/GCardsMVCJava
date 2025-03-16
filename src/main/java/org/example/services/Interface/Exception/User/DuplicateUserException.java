package org.example.services.Interface.Exception.User;

import org.example.services.Interface.Exception.ApplicationException;

public class DuplicateUserException extends ApplicationException {
    public DuplicateUserException(String message) {
        super(message);
    }
}