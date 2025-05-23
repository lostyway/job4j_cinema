package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.cinema.dto.FilmDto;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository implements IFilmRepository {

    @Override
    public List<FilmDto> getAllFilms() {
        return List.of();
    }

    @Override
    public Optional<FilmDto> getFilmById(int id) {
        return Optional.empty();
    }
}
