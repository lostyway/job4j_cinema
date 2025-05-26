package ru.job4j.cinema.repository;

import ru.job4j.cinema.dto.FilmDto;

import java.util.List;
import java.util.Optional;

public interface IFilmRepository {
    List<FilmDto> getAllFilms();

    Optional<FilmDto> getFilmById(int id);
}
