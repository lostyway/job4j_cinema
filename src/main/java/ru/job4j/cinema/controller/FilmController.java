package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cinema")
public class FilmController {

    @GetMapping("/films")
    public String films() {
        return "cinema/films";
    }
}
