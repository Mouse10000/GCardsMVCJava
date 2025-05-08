package org.example.controllers;

import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showProfile(Model model,@AuthenticationPrincipal User user) {
//        String username = authentication.getName();
//        User user = userService.findByUsername(username);

        model.addAttribute("user", user);

        return "auth/profile";
    }
}