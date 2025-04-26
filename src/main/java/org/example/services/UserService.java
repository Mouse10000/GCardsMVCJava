package org.example.services;

import org.example.models.Role;
import org.example.models.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        /*user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        List<Role> roleName = user.getRoles();
        for (Role roleList : roleName) {

            Role role = roleRepository.findByName(roleList.getName())
                    .orElseGet(() -> {
                        Role newRole = new Role(roleName);
                        return roleRepository.save(newRole);
                    });

        }


        user.setRole(role);

        userRepository.save(user);*/
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
