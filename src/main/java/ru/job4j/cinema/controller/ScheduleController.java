package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.SessionService;

import java.util.List;

@Controller
@RequestMapping("/cinema/schedule")
public class ScheduleController {
    private final SessionService sessionService;

    public ScheduleController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessionTimes")
    public String getSchedule(Model model) {
        List<String> times = sessionService.getTimes();
        model.addAttribute("sessions", times);
        return "/cinema/schedule/session-times";
    }
}
