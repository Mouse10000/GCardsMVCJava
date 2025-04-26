package org.example.dao.Interface;

import java.util.List;

public interface Role {
    void addRole(org.example.beans.Role role);
    org.example.beans.Role getRoleById(Long roleId);
    void updateRole(org.example.beans.Role role);
    void deleteRole(Long roleId);
    List<org.example.beans.Role> getAllRoles();
    void addUserRole(long userId, long roleId);
}
