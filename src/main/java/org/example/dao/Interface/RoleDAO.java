package org.example.dao.Interface;

import org.example.beans.Role;

import java.util.List;

public interface RoleDAO {
    void addRole(Role role);
    Role getRoleById(String roleId);
    void updateRole(Role role);
    void deleteRole(String roleId);
    List<Role> getAllRoles();
}
