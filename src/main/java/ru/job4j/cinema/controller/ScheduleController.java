package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cinema/schedule")
public class ScheduleController {

    @GetMapping("/movies")
    public String getSchedule() {
        return "cinema/schedule/movies";
    }
}
