package org.example.services.Interface.Exception.User;

import org.example.services.Interface.Exception.ApplicationException;

public class AuthenticationException extends ApplicationException {
    public AuthenticationException(String message) {
        super(message);
    }
}
