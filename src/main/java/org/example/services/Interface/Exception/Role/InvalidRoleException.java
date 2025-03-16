package org.example.services.Interface.Exception.Role;

import org.example.services.Interface.Exception.ApplicationException;

public class InvalidRoleException extends ApplicationException {
    public InvalidRoleException(String message) {
        super(message);
    }
}