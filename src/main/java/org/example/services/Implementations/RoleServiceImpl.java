package org.example.services.Implementations;

import org.example.beans.Role;
import org.example.beans.UserRole;
import org.example.dao.Interface.RoleDAO;
import org.example.dao.Interface.UserDAO;
import org.example.dao.Interface.UserRoleDAO;
import org.example.services.Interface.Exception.Role.DuplicateRoleException;
import org.example.services.Interface.Exception.Role.InvalidRoleException;
import org.example.services.Interface.Exception.Role.RoleNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;
    private final UserRoleDAO userRoleDAO;

    public RoleServiceImpl(RoleDAO roleDAO, UserDAO userDAO, UserRoleDAO userRoleDAO) {
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
        this.userRoleDAO = userRoleDAO;
    }

    @Override
    public void addRole(Role role) throws DuplicateRoleException, InvalidRoleException {
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new InvalidRoleException("Role name cannot be empty.");
        }
        if (roleDAO.getRoleById(role.getId()) != null) {
            throw new DuplicateRoleException("Role with name " + role.getName() + " already exists.");
        }
        roleDAO.addRole(role);
    }

    @Override
    public void updateRole(Role role)
            throws DuplicateRoleException, InvalidRoleException, RoleNotFoundException {
        // Проверка существования роли
        Role existingRole = roleDAO.getRoleById(role.getId());
        if (existingRole == null) {
            throw new RoleNotFoundException("Role with ID " + role.getId() + " not found.");
        }

        // Проверка на пустое имя
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new InvalidRoleException("Role name cannot be empty.");
        }

        // Проверка на дубликат имени (если имя изменилось)
        if (!existingRole.getName().equals(role.getName())){
            Role roleWithSameName = roleDAO.getRoleById(role.getId());
            if (roleWithSameName != null) {
                throw new DuplicateRoleException("Role with name " + role.getName() + " already exists.");
            }
        }
        // Обновление роли
        roleDAO.updateRole(role);
    }

    @Override
    public void deleteRole(Role role) throws RoleNotFoundException {
        // Проверка существования роли
        Role existingRole = roleDAO.getRoleById(role.getId());
        if (existingRole == null) {
            throw new RoleNotFoundException("Role with ID " + role.getId() + " not found.");
        }

        // Удаление роли
        roleDAO.deleteRole(role.getId());

    }

    @Override
    public void assignRoleToUser(long userId, long roleId)
            throws UserNotFoundException, RoleNotFoundException {
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        if (roleDAO.getRoleById(roleId) == null) {
            throw new RoleNotFoundException("Role with ID " + roleId + " not found.");
        }

        userRoleDAO.addUserRole(userId, roleId);
    }

    @Override
    public void removeRoleFromUser(long userId, long roleId) throws UserNotFoundException, RoleNotFoundException {
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        if (roleDAO.getRoleById(roleId) == null) {
            throw new RoleNotFoundException("Role with ID " + roleId + " not found.");
        }
        userRoleDAO.deleteUserRole(userId, roleId);
    }

    @Override
    public List<Role> getRolesByUserId(long userId) throws UserNotFoundException {
        if (userDAO.getUserById(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        return userRoleDAO.getRolesByUserId(userId);
    }
}
