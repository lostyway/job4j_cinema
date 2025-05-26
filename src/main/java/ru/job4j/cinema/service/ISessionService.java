package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.SessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ISessionService {
    List<SessionDto> getSessionsByFilmId(int filmId);

    SessionDto getSessionById(int sessionId);

    List<LocalDateTime> getUniqueSessionTimesOnNextWeek();

    List<SessionDto> getSessionsByStartTime(LocalDateTime startTime);

}
