package org.example.services.Interface;

import org.example.beans.Role;
import org.example.services.Interface.Exception.Role.RoleNotFoundException;
import org.example.services.Interface.Exception.Role.DuplicateRoleException;
import org.example.services.Interface.Exception.Role.InvalidRoleException;
import org.example.services.Interface.Exception.User.UserNotFoundException;

import java.util.List;

public interface RoleService {
    void addRole(Role role) throws DuplicateRoleException, InvalidRoleException;
    void updateRole(Role role) throws DuplicateRoleException, InvalidRoleException, RoleNotFoundException;
    void deleteRole(Role role) throws RoleNotFoundException;
    void assignRoleToUser(long userId, long roleId) throws UserNotFoundException, RoleNotFoundException;
    void removeRoleFromUser(long userId, long roleId) throws UserNotFoundException, RoleNotFoundException;
    List<Role> getRolesByUserId(long userId) throws UserNotFoundException;
}
