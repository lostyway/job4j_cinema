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
    public String getSchedule(Model model) {
        try {
            List<TimeDto> times = sessionService.getNextWeekSessionsTime();
            model.addAttribute("sessionTimes", times);
        } catch (Exception e) {
            model.addAttribute("error", "Сеансы не найдены");
            return "errors/404";
        }
        return "cinema/schedule/session-times";
    }

    @GetMapping("/sessions")
    public String getSessions(
            @RequestParam("time") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime, @RequestParam("sessionId") int sessionId,
            Model model) {
        try {
            List<FilmDto> films = filmService.findFilmsByStartTime(dateTime);
            model.addAttribute("films", films);
            model.addAttribute("time", dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            model.addAttribute("sessionId", sessionId);
            return "cinema/schedule/films-by-time";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке фильмов");
            return "errors/404";
        }
    }
}
