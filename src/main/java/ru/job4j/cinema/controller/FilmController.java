package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.SessionService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cinema")
public class FilmController {
    private final FilmService filmService;
    private final SessionService sessionService;

    public FilmController(FilmService filmService, SessionService sessionService) {
        this.filmService = filmService;
        this.sessionService = sessionService;
    }

    @GetMapping("/list")
    public String films(Model model) {
        List<FilmDto> films = filmService.getAllFilms();
        model.addAttribute("films", films);
        return "cinema/list";
    }

    @GetMapping("/one/{id}")
    public String filmsById(@PathVariable("id") int id, Model model) {
        FilmDto filmDto = filmService.getFilmById(id);
        List<SessionDto> sessions = sessionService.getSessionsByFilmId(id);
        sessions.forEach(SessionDto::fillRowsAndCount);

        model.addAttribute("film", filmDto);
        model.addAttribute("mySession", sessions);
        return "cinema/one";
    }

    @GetMapping("/sessions")
    public String sessionsById(@RequestParam("sessionId") int sessionId, Model model) {
        SessionDto sessionDto = sessionService.getSessionById(sessionId);
        sessionDto.fillRowsAndCount();

        model.addAttribute("selectedSession", sessionDto);
        model.addAttribute("sessionId", sessionId);
        return "cinema/selectSession";
    }
}
