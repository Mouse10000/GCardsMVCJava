package org.example.services.Interface.Exception.Role;

import org.example.services.Interface.Exception.ApplicationException;

public class DuplicateRoleException extends ApplicationException {
    public DuplicateRoleException(String message) {
        super(message);
    }
}
