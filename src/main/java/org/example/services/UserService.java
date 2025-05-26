package org.example.services;

import org.example.model.Role;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserRoleRepository;
import org.example.services.Interface.Exception.User.DuplicateUserException;
import org.example.services.Interface.Exception.User.InvalidUserException;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления пользователями системы.
 * Предоставляет методы для регистрации, аутентификации и управления пользователями.
 */
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

    /**
     * Регистрирует нового пользователя
     * @param user данные пользователя
     * @throws DuplicateUserException если пользователь уже существует
     * @throws InvalidUserException если данные пользователя некорректны
     */
    public void registerUser(User user) throws DuplicateUserException, InvalidUserException {
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

    /**
     * Аутентифицирует пользователя
     * @param username имя пользователя
     * @param password пароль
     * @return аутентифицированный пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    public User authenticateUser(String username, String password) throws UserNotFoundException {
        // ... existing code ...
        return null; // Placeholder return, actual implementation needed
    }

    /**
     * Обновляет информацию о пользователе
     * @param user обновленные данные пользователя
     * @throws UserNotFoundException если пользователь не найден
     * @throws InvalidUserException если данные пользователя некорректны
     */
    public void updateUser(User user) throws UserNotFoundException, InvalidUserException {
        // ... existing code ...
    }

    /**
     * Удаляет пользователя
     * @param userId ID пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    public void deleteUser(Long userId) throws UserNotFoundException {
        // ... existing code ...
    }
}
