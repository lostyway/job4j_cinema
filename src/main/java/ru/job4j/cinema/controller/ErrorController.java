package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/errors/404")
    public String error404(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error != null ? error : "Страница не найдена");
        return "errors/404";
    }
}
