package org.example.controllers.server;

import org.example.beans.Card;
import org.example.beans.Role;
import org.example.beans.User;
import org.example.models.UserForm;
import org.example.services.Interface.Exception.Card.CardNotFoundException;
import org.example.services.Interface.Exception.Role.RoleNotFoundException;
import org.example.services.Interface.Exception.User.*;
import org.example.services.Interface.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/users")
public class UserServerController {
    private final UserService userService;

    public UserServerController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping
    public List<User> listUsers() {
        return userService.getAllUsers();
    }*/

    @PostMapping
    @ResponseStatus(CREATED)
    public void registerUser(@RequestBody UserForm userForm)
            throws DuplicateUserException, InvalidUserException, NoSuchAlgorithmException {
        User user = new User();
        user.setUserName(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPasswordHash(userForm.getPassword());
        userService.registerUser(user);
    }

    @PostMapping("/authenticate")
    public User authenticateUser(@RequestBody AuthenticationRequest authRequest)
            throws AuthenticationException, NoSuchAlgorithmException {
        return userService.authenticateUser(authRequest.getUsername(), authRequest.getPassword());
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UserForm userForm)
            throws UserNotFoundException, InvalidUserException {
        User user = userService.getUserById(id);
        user.setUserName(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        if (userForm.getPassword() != null && !userForm.getPassword().isEmpty()) {
            user.setPasswordHash(userForm.getPassword());
        }
        userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return userService.getUserByUserName(username);
    }

    @GetMapping("/{userId}/cards")
    public List<Card> getUserCards(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getUserCards(userId);
    }

    @PostMapping("/{userId}/cards/{cardId}")
    @ResponseStatus(CREATED)
    public void addCardToUser(@PathVariable Long userId, @PathVariable Long cardId)
            throws UserNotFoundException, CardNotFoundException {
        userService.addCardToUser(userId, cardId);
    }

    @DeleteMapping("/{userId}/cards/{cardId}")
    @ResponseStatus(NO_CONTENT)
    public void removeCardFromUser(@PathVariable Long userId, @PathVariable Long cardId)
            throws UserNotFoundException, CardNotFoundException {
        userService.removeCardFromUser(userId, cardId);
    }

    @GetMapping("/{userId}/roles")
    public List<Role> getUserRoles(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getUserRoles(userId);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @ResponseStatus(CREATED)
    public void assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId)
            throws UserNotFoundException, RoleNotFoundException {
        userService.assignRoleToUser(userId, roleId);
    }

    // Вспомогательный класс для аутентификации
    private static class AuthenticationRequest {
        private String username;
        private String password;

        // Геттеры и сеттеры
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
