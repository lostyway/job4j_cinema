package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.dto.TimeDto;
import ru.job4j.cinema.service.SessionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cinema")
public class ScheduleController {
    private final SessionService sessionService;

    public ScheduleController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/schedule/sessionTimes")
    public String sessionTimes(Model model) {
        var times = sessionService.getUniqueSessionTimesOnNextWeek();
        List<TimeDto> dtoList = times.stream()
                .map(time -> new TimeDto(
                        time,
                        time.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))
                ))
                .toList();
        model.addAttribute("sessionTimes", dtoList);
        return "cinema/schedule/start_times";
    }

    @GetMapping("/schedule/sessionsByTime")
    public String sessionsByTime(
            @RequestParam("startTime") String startTimeRaw,
            Model model
    ) {
        var startTime = LocalDateTime.parse(startTimeRaw);
        var sessions = sessionService.getSessionsByStartTime(startTime);
        model.addAttribute("sessionsList", sessions);
        model.addAttribute("time", startTime);
        return "cinema/schedule/sessions_by_time";
    }

    @GetMapping("/selectSession")
    public String selectSession(
            @RequestParam("sessionId") int sessionId,
            Model model
    ) {
        SessionDto session = sessionService.getSessionById(sessionId);
        session.fillRowsAndCount();
        model.addAttribute("selectedSession", session);
        return "cinema/selectSession";
    }
}
