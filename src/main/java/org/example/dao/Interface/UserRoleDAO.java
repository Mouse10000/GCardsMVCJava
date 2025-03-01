package org.example.dao.Interface;

import org.example.beans.UserRole;

import java.util.List;

public interface UserRoleDAO {
    void addUserRole(UserRole userRole);
    UserRole getUserRoleById(String userId, String roleId);
    void updateUserRole(UserRole userRole);
    void deleteUserRole(String userId, String roleId);
    List<UserRole> getAllUserRoles();
    List<UserRole> getUserRolesByUserId(String userId);
}
