package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.SessionService;

import java.util.List;

@Controller
@RequestMapping("/cinema/schedule")
public class ScheduleController {
    private final SessionService sessionService;
    private final FilmService filmService;

    public ScheduleController(SessionService sessionService, FilmService filmService) {
        this.sessionService = sessionService;
        this.filmService = filmService;
    }

    @GetMapping("/sessionTimes")
    public String sessionTimes(Model model) {
        try {
            List<FilmDto> films = filmService.getAllFilmsInNextWeek();
            model.addAttribute("films", films);
            return "cinema/schedule/films";
        } catch (Exception e) {
            e.printStackTrace();
            return "errors/404";
        }
    }
}
