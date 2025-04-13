package org.example.controllers.oneway;


import org.example.models.StudentModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PracticeController {
    private final List<StudentModel> students = new ArrayList<>();

    public PracticeController() {
        // Заполните список пользователями
        this.students.add(new StudentModel(1, "First", "User", true, 4));
        this.students.add(new StudentModel(2, "Second", "User", false, 2));
        this.students.add(new StudentModel(3, "Third", "User", true, 5));
        this.students.add(new StudentModel(4, "Fourth", "User", false, 3));
    }

    @GetMapping("/second")
    public String secondView(Model model) {
        // Передача переменных в представление
        model.addAttribute("title", "Вторая страница");
        model.addAttribute("students", students);

        // Возврат имени представления
        return "practice/second";
    }

    @GetMapping("/third")
    public String showAddStudentForm(Model model) {
        model.addAttribute("model", new StudentModel());
        return "practice/third";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute("model") StudentModel model) throws UnsupportedEncodingException {
        // Дополнительная валидация: Проверка, что оценка не более 5.
        if (model.getMark() > 5) {
            // Если оценка больше 5, выполняется перенаправление с сообщением об ошибке.
            String errorMessage = URLEncoder.encode("Оценка должна быть от 1 до 5", "UTF-8");
            return "redirect:/third?error=" + errorMessage;
        } else {
            System.out.println("id" + model.getId());
            System.out.println("firstName" + model.getFirstName());
            System.out.println("sureName" + model.getSureName());
            System.out.println("isGraduated" + model.getIsGraduated());
            System.out.println("mark" + model.getMark());
            if (model.getIsGraduated() == null) model.setIsGraduated(false);
            students.add(model);

            return "redirect:/third?addedStudent=true";
        }
    }

    @GetMapping("/fourth")
    public String fourthView(Model model) {
        model.addAttribute("students", students);
        model.addAttribute("model", new StudentModel());
        return "practice/fourth";
    }
}