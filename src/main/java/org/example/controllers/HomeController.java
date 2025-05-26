package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для главной страницы приложения.
 * Обрабатывает запросы к корневому URL и отображает домашнюю страницу.
 */
@Controller
public class HomeController {
    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
} 