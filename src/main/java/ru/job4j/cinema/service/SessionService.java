package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDto;
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
            throw new RuntimeException("There is no session with film id " + filmId);
        }
        return sessions;
    }

    @Override
    public SessionDto getSessionById(int sessionId) {
        var sessionOpt = sessionRepository.getSessionById(sessionId);
        return sessionOpt.orElseThrow(() ->
                new RuntimeException("There is no session with id " + sessionId)
        );
    }

    @Override
    public List<SessionDto> getSessionsByStartTime(LocalDateTime startTime) {
        var sessions = sessionRepository.getSessionsByStartTime(startTime);
        sessions.forEach(sess -> {
            var film = filmService.getFilmById(sess.getFilmId());
            sess.setFilmDto(film);
        });
        return sessions;
    }

    @Override
    public List<LocalDateTime> getUniqueSessionTimesOnNextWeek() {
        return sessionRepository.getSessionTimesOnNextWeek();
    }
}
