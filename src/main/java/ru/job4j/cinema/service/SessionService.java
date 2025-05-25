package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.dto.TimeDto;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService implements ISessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<SessionDto> getSessionsByFilmId(int filmId) {
        var sessions = sessionRepository.getSessionsByFilmId(filmId);
        if (sessions.isEmpty()) {
            throw new RuntimeException("There is no session with film id " + filmId);
        }
        return sessions;
    }

    public SessionDto getSessionById(int sessionId) {
        var session = sessionRepository.getSessionById(sessionId);
        if (session.isEmpty()) {
            throw new RuntimeException("There is no session with id " + sessionId);
        }
        return session.get();
    }
}
