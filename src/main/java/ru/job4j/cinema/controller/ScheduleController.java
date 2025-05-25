package ru.job4j.cinema.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.TimeDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.SessionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        return "cinema/schedule/films";
    }
}
