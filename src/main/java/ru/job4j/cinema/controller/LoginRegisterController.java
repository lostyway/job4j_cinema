package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.exceptions.NotFoundException;
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
        try {
            User userToLogin = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
            request.getSession().setAttribute("user", userToLogin);
            request.getSession().setAttribute("userId", userToLogin.getId());
            return "redirect:/index";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "users/login";
        }  catch (Exception e) {
            log.error("Необработанное исключение в методе loginByEmailAndPassword", e);
            model.addAttribute("error", "Произошла непредвиденная ошибка. Попробуйте позже");
            return "errors/404";
        }
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute User user, Model model) {
        try {
            userService.save(user);
            return "redirect:/users/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (Exception e) {
            log.error("Необработанное исключение в методе register", e);
            model.addAttribute("error", "Произошла непредвиденная ошибка. Попробуйте позже");
            return "errors/404";
        }
    }
}
