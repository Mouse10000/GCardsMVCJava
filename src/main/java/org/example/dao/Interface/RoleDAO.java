package org.example.dao.Interface;

import org.example.beans.Role;

import java.util.List;

public interface RoleDAO {
    void addRole(Role role);
    Role getRoleById(Long roleId);
    void updateRole(Role role);
    void deleteRole(Long roleId);
    List<Role> getAllRoles();
    void addUserRole(long userId, long roleId);
}
