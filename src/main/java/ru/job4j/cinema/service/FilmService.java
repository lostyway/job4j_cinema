package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService implements IFilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public List<FilmDto> getAllFilms() {
        return List.of();
    }

    @Override
    public Optional<FilmDto> getFilmById(int id) {
        return Optional.empty();
    }
}
