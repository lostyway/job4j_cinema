package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.repository.FilmRepository;

import java.time.LocalDateTime;
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
        List<FilmDto> result = filmRepository.getAllFilms();
        if (result.isEmpty()) {
            throw new RuntimeException("No films found");
        }
        return result;
    }

    @Override
    public FilmDto getFilmById(int id) {
        Optional<FilmDto> result = filmRepository.getFilmById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("No film found");
        }
        return result.get();
    }

    public List<FilmDto> findFilmsByStartTime(LocalDateTime dateTime) {
        var listOfFilms = filmRepository.findFilmsByStartTime(dateTime);
        if (listOfFilms.isEmpty()) {
            throw new RuntimeException("No films found");
        }
        return listOfFilms;
    }
}
