package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> getTimes() {
        List<String> times = sessionRepository.getSessionByTimes();
        if (times.isEmpty()) {
            throw new RuntimeException("There is no session with times ");
        }
        return times;
    }

    private List<SessionDto> getSessionsByTime(String time) {
        List<SessionDto> sessionDto = sessionRepository.getSessionsByCurrentTime(time);
        if (sessionDto.isEmpty()) {
            throw new RuntimeException("There is no session with current time " + time);
        }
        return sessionDto;
    }
}
