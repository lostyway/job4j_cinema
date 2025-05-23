package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/one/{id}")
    public String filmsById(@PathVariable("id") int id, Model model) {
        try {
            System.out.println("filmById called with id = " + id);
            Optional<FilmDto> filmDto = filmService.getFilmById(id);
            model.addAttribute("film", filmDto.get());
        } catch (RuntimeException e) {
            model.addAttribute("error", "Фильм не был найден");
            return "errors/404";
        }
        return "cinema/one";
    }
}
