package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

@Controller
@RequestMapping("/users")
public class LoginRegisterController {
    private final UserService userService;

    public LoginRegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @GetMapping("register")
    public String getRegisterPage() {
        return "users/register";
    }

    @GetMapping("/logout")
    public String makeLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
        try {
            User userToLogin = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
            request.getSession().setAttribute("user", userToLogin);
            return "redirect:/index";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            return "users/login";
        }
    }

    @PostMapping("/register")
    public String login(@ModelAttribute User user, Model model) {
        try {
            User userToRegister = userService.save(user);
            return "redirect:/users/login";
        } catch (Exception e) {
            model.addAttribute("error", String.format("Пользователь с почтой %s уже существует", user.getEmail()));
            return "errors/404";
        }
    }
}
