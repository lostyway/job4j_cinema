package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRegisterController {

    @GetMapping("users/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("users/register")
    public String register() {
        return "users/register";
    }

    @GetMapping("users/logout")
    public String makeRegister() {
        return "users/login";
    }
}
