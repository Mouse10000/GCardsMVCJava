package org.example.controllers.auth;

import org.example.model.User;
import org.example.services.Interface.Exception.User.DuplicateUserException;
import org.example.services.Interface.Exception.User.InvalidUserException;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для обработки регистрации новых пользователей.
 * Управляет процессом создания новых учетных записей и валидацией данных.
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserService userService;

    /**
     * Отображает страницу регистрации
     * @return имя представления для страницы регистрации
     */
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    /**
     * Обрабатывает отправку формы регистрации
     * @param user данные пользователя из формы
     * @param bindingResult результаты валидации
     * @return перенаправление на страницу входа при успешной регистрации
     */
    @PostMapping
    public String registerUser( User user, BindingResult bindingResult) throws InvalidUserException, DuplicateUserException {
        if (userService.findByUsername(user.getUsername()) != null) {
            return "auth/register";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }
}