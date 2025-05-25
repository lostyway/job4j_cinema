package ru.job4j.cinema.repository;

import ru.job4j.cinema.dto.SessionDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISessionRepository {
    List<SessionDto> getSessionsByFilmId(int filmId);

    Optional<SessionDto> getSessionById(int sessionId);
}
