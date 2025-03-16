package org.example.services.Interface.Exception.User;

import org.example.services.Interface.Exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
