package org.example.controllers;

import org.example.models.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final List<UserForm> users = new ArrayList<>();

    public AuthController() {
        // Пример 1: Молодой мужчина
        UserForm user1 = new UserForm(
                "john_doe",
                "john.doe@example.com",
                "securePass123",
                "securePass123",
                25,
                "male"
        );

        // Пример 2: Женщина среднего возраста
        UserForm user2 = new UserForm("jane_smith","jane.smith@example.com",
                "janePassword!","janePassword!",34,"female"
        );

        // Пример 3: Подросток (другой пол)
        UserForm user3 = new UserForm("alex_young","alex.y@example.com",
                "youngAlex2023","youngAlex2023",17,"other"
        );

        // Пример 4: Пожилой пользователь
        UserForm user4 = new UserForm("grandpa_user","grandpa@example.com",
                "oldbutgold", "oldbutgold",72,"male"
        );
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
    }
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new UserForm());
        model.addAttribute("pageTitle", "page.login");
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserForm user) {
        for (UserForm existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername()) &&
                    existingUser.getPassword().equals(user.getPassword())) {
                return "redirect:/auth/users";
            }
        }
        return "redirect:/login?error=Invalid username or password";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerForm", new UserForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("registerForm") UserForm form,
            RedirectAttributes redirectAttributes) {

        // Валидация вручную
        if (form.getUsername() == null || form.getUsername().trim().isEmpty()) {
            redirectAttributes.addAttribute("error", "Username is required");
            return "redirect:/auth/register";
        }

        if (form.getEmail() == null || !form.getEmail().contains("@")) {
            redirectAttributes.addAttribute("error", "Invalid email address");
            return "redirect:/auth/register";
        }

        if (form.getPassword() == null || form.getPassword().length() < 6) {
            redirectAttributes.addAttribute("error", "Password must be at least 6 characters");
            return "redirect:/auth/register";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            redirectAttributes.addAttribute("error", "Passwords do not match");
            return "redirect:/auth/register";
        }

        if (form.getAge() < 13 || form.getAge() > 120) {
            redirectAttributes.addAttribute("error", "Age must be between 13 and 120");
            return "redirect:/auth/register";
        }

        if (form.getMale() == null || form.getMale().isEmpty()) {
            redirectAttributes.addAttribute("error", "Please select gender");
            return "redirect:/auth/register";
        }

        // Логика сохранения пользователя
        // userService.register(form);
        users.add(form);
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/auth/register";
    }



    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", users);
        return "auth/users";
    }

    @GetMapping("/modify-user")
    public String showModifyUserForm(@RequestParam("username") String username, Model model) {
        UserForm user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        if (user == null) {
            return "redirect:/users?error=User not found";
        }
        model.addAttribute("user", user);
        return "auth/modify-user";
    }

    @PostMapping("/modify-user")
    public String modifyUser(@ModelAttribute("user") UserForm updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
        return "redirect:/auth/users";
    }

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam("username") String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        return "redirect:/auth/users";
    }

}