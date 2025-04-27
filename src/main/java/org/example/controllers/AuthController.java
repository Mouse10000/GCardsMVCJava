package org.example.controllers;

import org.example.models.User;
import org.example.services.UserService;
import org.example.services.Interface.Exception.User.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "auth/users";
    }

    @GetMapping("/modify-user")
    public String modifyUser(@RequestParam String username, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUserName(username);
            model.addAttribute("user", user);
            return "auth/modify-user";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/auth/users";
        }
    }

    @PostMapping("/modify-user")
    public String updateUser(User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
            return "redirect:/auth/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/modify-user?username=" + user.getUsername();
        }
    }

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam String username, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUserName(username);
            userService.deleteUser(user.getId());
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/auth/users";
    }
} 