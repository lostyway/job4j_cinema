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
import ru.job4j.cinema.exceptions.NotFoundException;
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
        try {
            List<FilmDto> films = filmService.getAllFilms();
            model.addAttribute("films", films);
            return "cinema/list";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (Exception e) {
            log.error("Необработанное исключение в методе films", e);
            model.addAttribute("error", "Произошла непредвиденная ошибка. Попробуйте позже");
            return "errors/404";
        }
    }

    @GetMapping("/one/{id}")
    public String  filmsById(@PathVariable("id") int id, Model model) {
        try {
            FilmDto filmDto = filmService.getFilmById(id);
            List<SessionDto> sessions = sessionService.getSessionsByFilmId(id);
            sessions.forEach(SessionDto::fillRowsAndCount);

            model.addAttribute("film", filmDto);
            model.addAttribute("mySession", sessions);
            return "cinema/one";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (Exception e) {
            log.error("Необработанное исключение в методе filmsById", e);
            model.addAttribute("error", "Произошла непредвиденная ошибка. Попробуйте позже");
            return "errors/404";
        }
    }

    @GetMapping("/sessions")
    public String sessionsById(@RequestParam("sessionId") int sessionId, Model model) {
        try {
            SessionDto sessionDto = sessionService.getSessionById(sessionId);
            sessionDto.fillRowsAndCount();

            model.addAttribute("selectedSession", sessionDto);
            model.addAttribute("sessionId", sessionId);
            return "cinema/selectSession";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (Exception e) {
            log.error("Необработанное исключение в методе sessionsById", e);
            model.addAttribute("error", "Произошла непредвиденная ошибка. Попробуйте позже");
            return "errors/404";
        }
    }
}
