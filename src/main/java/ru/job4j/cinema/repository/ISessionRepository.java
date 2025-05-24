package ru.job4j.cinema.repository;

import ru.job4j.cinema.dto.SessionDto;

import java.util.List;

public interface ISessionRepository {
    List<SessionDto> getSessionsByFilmId(int filmId);
}
