package org.example.repository;

import org.example.model.Role;
import org.example.model.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    @Query("select ur.role from UserRole ur where ur.user.id = :userId")
    @EntityGraph(value = "UserRole.role", type = EntityGraph.EntityGraphType.LOAD)
    List<Role> findByUserId(@Param("userId") Long userId);
}