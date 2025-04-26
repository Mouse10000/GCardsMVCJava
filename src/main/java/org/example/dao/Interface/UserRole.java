package org.example.dao.Interface;

import org.example.beans.Role;
import org.example.beans.User;

import java.util.List;

public interface UserRole {
    void addUserRole(long userId, long roleId);
    void deleteUserRole(long userId, long roleId);
    List<Role> getRolesByUserId(long userId);
    List<User> getUsersByRoleId(long roleId);
}