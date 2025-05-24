package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmDto;

import java.util.List;

public interface IFilmService {
    List<FilmDto> getAllFilms();

    FilmDto getFilmById(int id);
}
