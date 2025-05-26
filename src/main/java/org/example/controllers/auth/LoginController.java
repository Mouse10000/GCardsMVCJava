package org.example.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки аутентификации пользователей.
 * Управляет процессом входа в систему и отображением страницы входа.
 */
@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}