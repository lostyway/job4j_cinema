package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.dto.FilmDto;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository implements IFilmRepository {
    private final Sql2o sql2o;

    public FilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<FilmDto> getAllFilms() {
        String sql = """
                SELECT f.id, f.name, f.description, f.year,
                       f.minimal_age as minimalAge,
                       f.duration_in_minutes as durationInMinutes, g.name as genre, files.path as filePath
                FROM films f
                JOIN genres g on g.id = f.genre_id
                JOIN files on files.id = f.file_id
                """;
        try (Connection con = sql2o.open()) {
            Query query = con.createQuery(sql);
            return query.executeAndFetch(FilmDto.class);
        }
    }

    @Override
    public Optional<FilmDto> getFilmById(int id) {
        String sql = "SELECT * FROM films WHERE id = :id";
        try (Connection con = sql2o.open()) {
            Query query = con.createQuery(sql).addParameter("id", id);
            return Optional.ofNullable(query.executeAndFetchFirst(FilmDto.class));
        }
    }
}
