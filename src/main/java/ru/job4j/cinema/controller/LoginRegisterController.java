package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

@Slf4j
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
    public String loginByEmailAndPassword(@ModelAttribute User user, Model model, HttpServletRequest request) {

        User userToLogin = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        request.getSession().setAttribute("user", userToLogin);
        request.getSession().setAttribute("userId", userToLogin.getId());
        return "redirect:/index";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute User user, Model model) {

        userService.save(user);
        return "redirect:/users/login";
    }
}
