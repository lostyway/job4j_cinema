package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;

@Controller
@RequestMapping("/cinema")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public String films(Model model) {
        try {
            List<FilmDto> films = filmService.getAllFilms();
            model.addAttribute("films", films);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
        return "cinema/list";
    }
}
