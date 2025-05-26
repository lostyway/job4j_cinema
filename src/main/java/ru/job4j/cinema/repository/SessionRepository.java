package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.dto.SessionDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionRepository implements ISessionRepository {
    private final Sql2o sql2o;

    public SessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<SessionDto> getSessionsByFilmId(int filmId) {
        var sql = """
                select s.id, s.film_id as filmId, h.name as hallName, s.start_time as startTime, s.end_time as endTime, s.price, h.row_count as rowCount, h.place_count as placeCount
                from film_sessions s
                join halls h on h.id = s.halls_id
                where s.film_id = :filmId
                order by s.start_time
                """;
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("filmId", filmId)
                    .executeAndFetch(SessionDto.class);
        }
    }

    @Override
    public Optional<SessionDto> getSessionById(int sessionId) {
        var sql = """
                select s.id, s.film_id as filmId, h.name as hallName, s.start_time as startTime, s.end_time as endTime, s.price, h.row_count as rowCount, h.place_count as placeCount
                from film_sessions s
                join halls h on h.id = s.halls_id
                where s.id = :sessionId
                """;
        try (Connection con = sql2o.open()) {
            return Optional.ofNullable(con.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .executeAndFetchFirst(SessionDto.class));
        }
    }

    @Override
    public List<LocalDateTime> getSessionTimesOnNextWeek() {
        var sql = """
            SELECT DISTINCT start_time
            FROM film_sessions
            WHERE start_time >= :now AND start_time < :weekLater
            ORDER BY start_time
            """;
        try (Connection con = sql2o.open()) {
            var now = LocalDateTime.now();
            var weekLater = now.plusDays(7);
            return con.createQuery(sql)
                    .addParameter("now", now)
                    .addParameter("weekLater", weekLater)
                    .executeAndFetch(LocalDateTime.class);
        }
    }

    @Override
    public List<SessionDto> getSessionsByStartTime(LocalDateTime startTime) {
        var sql = """
            SELECT s.id, s.film_id AS filmId, h.name AS hallName,
                   s.start_time AS startTime, s.end_time AS endTime,
                   s.price, h.row_count AS rowCount, h.place_count AS placeCount
            FROM film_sessions s
            JOIN halls h ON h.id = s.halls_id
            WHERE s.start_time = :startTime
            """;
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("startTime", startTime)
                    .executeAndFetch(SessionDto.class);
        }
    }
}
