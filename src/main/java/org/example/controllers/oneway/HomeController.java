package org.example.controllers.oneway;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Главная");
        return "/welcome";
    }
    @GetMapping("/about")
    public String about() {
        return "pages/about";
    }
}
