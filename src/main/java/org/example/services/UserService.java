package org.example.services;

import org.example.model.Role;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserRoleRepository;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("USER");
        if (role == null) roleRepository.save(new Role("USER"));
        userRepository.save(user);
        userRoleRepository.save(new UserRole(user, role));
        user.addRole(role);
    }
    public List<User> getAllUsersWithoutOne(String username) throws UserNotFoundException {
        Optional<User> userSender = userRepository.findByUsername(username);
        if (userSender.isEmpty()) throw new UserNotFoundException("User Recipient not found");
        return userRepository.findAllByUsernameNot(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
