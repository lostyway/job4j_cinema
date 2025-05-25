package ru.job4j.cinema.controller;

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

@Controller
@RequestMapping("/cinema")
public class FilmController {
    private final FilmService filmService;
    private final SessionService sessionService;

    public FilmController(FilmService filmService, SessionService sessionService) {
        this.filmService = filmService;
        this.sessionService = sessionService;
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
            FilmDto filmDto = filmService.getFilmById(id);
            List<SessionDto> sessions = sessionService.getSessionsByFilmId(id);
            sessions.forEach(SessionDto::fillRowsAndCount);

            model.addAttribute("film", filmDto);
            model.addAttribute("mySession", sessions);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Фильм не был найден");
            return "errors/404";
        }
        return "cinema/one";
    }

    @GetMapping("/sessions/{sessionId}")
    public String filmsBySession(@PathVariable int sessionId, Model model) {
        SessionDto sessionDto = sessionService.getSessionById(sessionId);
        sessionDto.fillRowsAndCount();

        model.addAttribute("session", sessionDto);
        return "cinema/selectSession";
    }

    @GetMapping("/sessions")
    public String sessionsById(@RequestParam("sessionId") int sessionId, Model model) {
        SessionDto sessionDto = sessionService.getSessionById(sessionId);
        sessionDto.fillRowsAndCount();

        model.addAttribute("mySession", sessionDto);
        model.addAttribute("sessionId", sessionId);
        return "cinema/selectSession";
    }

    @GetMapping("/sessions/{time}")
    public String filmsByTime(@PathVariable String time, Model model) {
        return "/cinema/schedule/films-by-time";
    }
}
