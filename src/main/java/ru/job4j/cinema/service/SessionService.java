package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService implements ISessionService {
    private final SessionRepository sessionRepository;
    private final FilmService filmService;

    public SessionService(SessionRepository sessionRepository, FilmService filmService) {
        this.sessionRepository = sessionRepository;
        this.filmService = filmService;
    }

    @Override
    public List<SessionDto> getSessionsByFilmId(int filmId) {
        var sessions = sessionRepository.getSessionsByFilmId(filmId);
        if (sessions.isEmpty()) {
            throw new NotFoundException("Не было найдено сеансов для фильма: " + filmId);
        }
        return sessions;
    }

    @Override
    public SessionDto getSessionById(int sessionId) {
        var sessionOpt = sessionRepository.getSessionById(sessionId);
        return sessionOpt.orElseThrow(() ->
                new NotFoundException("Не было найдено сессий с id: " + sessionId)
        );
    }

    @Override
    public List<SessionDto> getSessionsByStartTime(LocalDateTime startTime) {
        List<SessionDto> sessions = sessionRepository.getSessionsByStartTime(startTime);

        if (sessions.isEmpty()) {
            throw new NotFoundException("Не было найдено сессий с временем начала: " + startTime);
        }

        sessions.forEach(sess -> {
            FilmDto film = filmService.getFilmById(sess.getFilmId());
            sess.setFilmDto(film);
        });

        return sessions;
    }

    @Override
    public List<LocalDateTime> getUniqueSessionTimesOnNextWeek() {
        List<LocalDateTime> sessionOnWeek = sessionRepository.getSessionTimesOnNextWeek();
        if (sessionOnWeek.isEmpty()) {
            throw new NotFoundException("Нет сессий с фильмами на этой неделе");
        }
        return sessionOnWeek;
    }
}
