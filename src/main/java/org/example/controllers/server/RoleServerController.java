package org.example.controllers.server;

import org.example.beans.Role;
import org.example.models.RoleForm;
import org.example.services.Interface.Exception.Role.DuplicateRoleException;
import org.example.services.Interface.Exception.Role.InvalidRoleException;
import org.example.services.Interface.Exception.Role.RoleNotFoundException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.example.services.Interface.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/roles")
public class RoleServerController {
    private final RoleService roleService;

    public RoleServerController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> listRoles() throws RoleNotFoundException {
        return roleService.getAllRoles();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void addRole(@RequestBody RoleForm roleForm)
            throws DuplicateRoleException, InvalidRoleException {
        Role role = new Role();
        role.setName(roleForm.getName());
        roleService.addRole(role);
    }

    @PutMapping("/{id}")
    public void updateRole(@PathVariable Long id, @RequestBody RoleForm roleForm)
            throws DuplicateRoleException, InvalidRoleException, RoleNotFoundException {
        Role role = roleService.getRoleById(id);
        role.setName(roleForm.getName());
        roleService.updateRole(role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteRole(@PathVariable Long id) throws RoleNotFoundException {
        Role role = roleService.getRoleById(id);
        roleService.deleteRole(role);
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) throws RoleNotFoundException {
        return roleService.getRoleById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Role> getRolesByUserId(@PathVariable Long userId) throws UserNotFoundException {
        return roleService.getRolesByUserId(userId);
    }

    @PostMapping("/{roleId}/user/{userId}")
    @ResponseStatus(CREATED)
    public void assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId)
            throws UserNotFoundException, RoleNotFoundException {
        roleService.assignRoleToUser(userId, roleId);
    }

    @DeleteMapping("/{roleId}/user/{userId}")
    @ResponseStatus(NO_CONTENT)
    public void removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId)
            throws UserNotFoundException, RoleNotFoundException {
        roleService.removeRoleFromUser(userId, roleId);
    }
}