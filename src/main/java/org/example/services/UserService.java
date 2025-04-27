package org.example.services;

import org.example.models.Role;
import org.example.models.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.services.Interface.Exception.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(User user) throws DuplicateUserException, InvalidUserException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException("Username already exists");
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new InvalidUserException("Username cannot be empty");
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            throw new InvalidUserException("Password cannot be empty");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // Assign default role if no roles are specified
        if (user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("USER");
                        return roleRepository.save(newRole);
                    });
            user.addRole(defaultRole);
        }

        userRepository.save(user);
    }

    public User authenticateUser(String username, String password) throws AuthenticationException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return user;
    }

    @Transactional
    public void updateUser(User user) throws UserNotFoundException, InvalidUserException {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getPasswordHash() != null && !user.getPasswordHash().trim().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }

        userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public User getUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User getUserByUserName(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
