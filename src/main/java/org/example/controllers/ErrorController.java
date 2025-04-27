package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            switch (error) {
                case "access-denied":
                    model.addAttribute("errorMessage", "Access denied. You don't have permission to access this resource.");
                    break;
                case "invalid-session":
                    model.addAttribute("errorMessage", "Your session has expired. Please login again.");
                    break;
                default:
                    model.addAttribute("errorMessage", "An error occurred. Please try again later.");
            }
        } else {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        }
        return "error";
    }
} 