package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.SessionService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmControllerTest {
    private FilmService filmService;
    private SessionService sessionService;
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmService = mock(FilmService.class);
        sessionService = mock(SessionService.class);
        filmController = new FilmController(filmService, sessionService);
    }

    @Nested
    class WhenTestMethodFilms {

        @Test
        public void whenFilmsSuccessThenReturnCinemaFilmsAndGoodView() {
            List<FilmDto> filmDto = List.of(new FilmDto());
            when(filmService.getAllFilms()).thenReturn(filmDto);

            var model = new ConcurrentModel();
            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isEqualTo(filmDto);
            assertThat(view).isEqualTo("cinema/list");
        }

        @Test
        public void whenFilmsSuccessV2ThenReturnCinemaFilmsAndGoodView() {
            List<FilmDto> filmDto = List.of(new FilmDto(1, "name", "desc", 2002, 16, 120, "genre", "filepath"), new FilmDto());
            when(filmService.getAllFilms()).thenReturn(filmDto);

            var model = new ConcurrentModel();
            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isEqualTo(filmDto);
            Object result = model.getAttribute("films");
            assertThat(result).isInstanceOf(List.class);
            @SuppressWarnings("unchecked")
            List<FilmDto> resList = (List<FilmDto>) result;
            assertThat(resList.get(0).getName()).isEqualTo("name");
            assertThat(resList.get(0).getDurationInMinutes()).isEqualTo(120);
            assertThat(model.getAttribute("films")).isEqualTo(filmDto);
            assertThat(view).isEqualTo("cinema/list");
        }

        @Test
        public void whenFilmsNotFoundThenReturnExceptionAndErrorView() {
            when(filmService.getAllFilms()).thenThrow(new NotFoundException("Фильм не был найден"));

            var model = new ConcurrentModel();
            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isNull();
            assertThat(model.getAttribute("error")).isEqualTo("Фильм не был найден");
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenFilmsThrowGlobalExceptionThenReturnExceptionAndErrorView() {
            when(filmService.getAllFilms()).thenThrow(new RuntimeException("ошибка"));

            var model = new ConcurrentModel();
            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isNull();
            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
            assertThat(view).isEqualTo("errors/404");
        }
    }

    @Nested
    class WhenTestMethodFilmsById {
    }
}