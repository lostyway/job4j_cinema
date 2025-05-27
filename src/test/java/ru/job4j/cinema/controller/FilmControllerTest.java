package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.SessionDto;
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
    private FilmDto filmDto;
    private SessionDto sessionDto;
    private Model model;

    @BeforeEach
    public void setUp() {
        filmService = mock(FilmService.class);
        sessionService = mock(SessionService.class);
        filmController = new FilmController(filmService, sessionService);
        model = new ConcurrentModel();
    }

    @Nested
    class WhenTestMethodFilms {

        @Test
        public void whenFilmsSuccessThenReturnCinemaFilmsAndGoodView() {
            List<FilmDto> filmDto = List.of(new FilmDto());
            when(filmService.getAllFilms()).thenReturn(filmDto);

            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isEqualTo(filmDto);
            assertThat(view).isEqualTo("cinema/list");
        }

        @Test
        public void whenFilmsSuccessV2ThenReturnCinemaFilmsAndGoodView() {
            List<FilmDto> filmDto = List.of(new FilmDto(1, "name", "desc", 2002, 16, 120, "genre", "filepath"), new FilmDto());
            when(filmService.getAllFilms()).thenReturn(filmDto);

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

            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isNull();
            assertThat(model.getAttribute("error")).isEqualTo("Фильм не был найден");
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenFilmsThrowGlobalExceptionThenReturnExceptionAndErrorView() {
            when(filmService.getAllFilms()).thenThrow(new RuntimeException("ошибка"));

            var view = filmController.films(model);

            assertThat(model.getAttribute("films")).isNull();
            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
            assertThat(view).isEqualTo("errors/404");
        }
    }

    @Nested
    class WhenTestMethodFilmsById {

        @BeforeEach
        public void setUp() {
            filmDto = new FilmDto();
            sessionDto = new SessionDto();
        }

        @Test
        public void whenMethodIsSuccessThenReturnFilmAndSessionsDto() {
            int id = 1;
            when(filmService.getFilmById(id)).thenReturn(filmDto);
            when(sessionService.getSessionsByFilmId(id)).thenReturn(List.of(sessionDto));

            var view = filmController.filmsById(id, model);

            assertThat(model.getAttribute("film")).isEqualTo(filmDto);
            assertThat(model.getAttribute("mySession")).isEqualTo(List.of(sessionDto));
            assertThat(view).isEqualTo("cinema/one");
        }

        @Test
        public void whenGetFilmByIdIsFailedByGlobalExceptionThenReturnExceptionAndErrorView() {
            int id = 1;
            when(filmService.getFilmById(id)).thenThrow(new RuntimeException("global exception"));
            when(sessionService.getSessionsByFilmId(id)).thenReturn(List.of(sessionDto));

            var view = filmController.filmsById(id, model);

            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
            assertThat(model.getAttribute("mySession")).isEqualTo(null);
            assertThat(model.getAttribute("film")).isEqualTo(null);
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenGetFilmByIdIsFailedByNotFoundExceptionThenReturnExceptionAndErrorView() {
            int id = 1;
            when(filmService.getFilmById(id)).thenThrow(new NotFoundException("Фильм не был найден"));
            when(sessionService.getSessionsByFilmId(id)).thenReturn(List.of(sessionDto));

            var view = filmController.filmsById(id, model);

            assertThat(model.getAttribute("error")).isEqualTo("Фильм не был найден");
            assertThat(model.getAttribute("mySession")).isEqualTo(null);
            assertThat(model.getAttribute("film")).isEqualTo(null);
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenGetSessionsByFilmIdFailedByGlobalExceptionThenReturnExceptionAndErrorView() {
            int id = 1;
            when(filmService.getFilmById(id)).thenReturn(filmDto);
            when(sessionService.getSessionsByFilmId(id)).thenThrow(new RuntimeException("error"));

            var view = filmController.filmsById(id, model);

            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
            assertThat(model.getAttribute("mySession")).isEqualTo(null);
            assertThat(model.getAttribute("film")).isEqualTo(null);
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenGetSessionsByFilmIdFailedByNotFoundExceptionThenReturnExceptionAndErrorView() {
            int id = 1;
            when(filmService.getFilmById(id)).thenReturn(filmDto);
            when(sessionService.getSessionsByFilmId(id)).thenThrow(new NotFoundException("Не было найдено сеансов для фильма: " + id));

            var view = filmController.filmsById(id, model);

            assertThat(model.getAttribute("error")).isEqualTo("Не было найдено сеансов для фильма: " + id);
            assertThat(model.getAttribute("mySession")).isEqualTo(null);
            assertThat(model.getAttribute("film")).isEqualTo(null);
            assertThat(view).isEqualTo("errors/404");
        }
    }

    @Nested
    class WhenTestMethodSessionsById {

        @BeforeEach
        public void setUp() {
            sessionDto = new SessionDto();
        }

        @Test
        public void whenSessionsByIdIsSuccessThenReturnSessionDtoAndSessionId() {
            int sessionIdForTest = 10;
            when(sessionService.getSessionById(sessionIdForTest)).thenReturn(sessionDto);

            var view = filmController.sessionsById(sessionIdForTest, model);

            assertThat(model.getAttribute("selectedSession")).isEqualTo(sessionDto);
            assertThat(model.getAttribute("sessionId")).isEqualTo(sessionIdForTest);
            assertThat(view).isEqualTo("cinema/selectSession");
        }

        @Test
        public void whenSessionsByIdIsFailedByGlobalExceptionThenReturnExceptionAndErrorView() {
            int sessionIdForTest = 10;
            when(sessionService.getSessionById(sessionIdForTest)).thenThrow(new RuntimeException("error"));

            var view = filmController.sessionsById(sessionIdForTest, model);

            assertThat(model.getAttribute("selectedSession")).isEqualTo(null);
            assertThat(model.getAttribute("sessionId")).isEqualTo(null);
            assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
            assertThat(view).isEqualTo("errors/404");
        }

        @Test
        public void whenSessionsByIdIsFailedByNotFoundExceptionThenReturnExceptionAndErrorView() {
            int sessionIdForTest = 10;
            when(sessionService.getSessionById(sessionIdForTest))
                    .thenThrow(new NotFoundException("Не было найдено сессий с id: " + sessionIdForTest));

            var view = filmController.sessionsById(sessionIdForTest, model);

            assertThat(model.getAttribute("selectedSession")).isEqualTo(null);
            assertThat(model.getAttribute("sessionId")).isEqualTo(null);
            assertThat(model.getAttribute("error")).isEqualTo("Не было найдено сессий с id: " + sessionIdForTest);
            assertThat(view).isEqualTo("errors/404");
        }
    }
}