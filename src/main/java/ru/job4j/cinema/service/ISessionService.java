package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.dto.TimeDto;

import java.util.List;

public interface ISessionService {
    List<SessionDto> getSessionsByFilmId(int filmId);

    SessionDto getSessionById(int sessionId);

    List<TimeDto> getNextWeekSessionsTime();
}
