package org.example.services.Interface.Exception.Role;

import org.example.services.Interface.Exception.ApplicationException;

public class RoleNotFoundException extends ApplicationException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
