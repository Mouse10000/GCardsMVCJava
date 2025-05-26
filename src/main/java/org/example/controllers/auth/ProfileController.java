package org.example.controllers.auth;

import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для управления профилем пользователя.
 * Обрабатывает запросы на просмотр и редактирование профиля пользователя.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    /**
     * Отображает страницу профиля пользователя
     * @param model модель для передачи данных в представление
     * @return имя представления для страницы профиля
     */
    @GetMapping
    //@PreAuthorize("hasAuthority('ADMIN')")
    public String showProfile(Model model, @AuthenticationPrincipal User user) {
//        String username = authentication.getName();
//        User user = userService.findByUsername(username);

        model.addAttribute("user", user);

        return "auth/account";
    }

    /**
     * Обрабатывает обновление профиля пользователя
     * @param user обновленные данные пользователя
     * @return перенаправление на страницу профиля
     */
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user) {
        // ... existing code ...
        return "redirect:/profile";
    }
}