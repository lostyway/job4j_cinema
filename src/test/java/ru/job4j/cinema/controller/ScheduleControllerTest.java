package ru.job4j.cinema.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.dto.TimeDto;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.service.SessionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScheduleControllerTest {
    private SessionService sessionService;
    private ScheduleController controller;

    @BeforeEach
    void setUp() {
        sessionService = mock(SessionService.class);
        controller = new ScheduleController(sessionService);
    }

    @Test
    public void testWhenSessionTimesReturnException() {
        when(sessionService.getUniqueSessionTimesOnNextWeek()).thenThrow(new NotFoundException("Нет сессий с фильмами на этой неделе"));

        var model = new ConcurrentModel();
        var view = controller.sessionTimes(model);

        assertThat(model.getAttribute("error")).isEqualTo("Нет сессий с фильмами на этой неделе");
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void testWhenSessionTimesReturnParseException() {
        when(sessionService.getUniqueSessionTimesOnNextWeek()).thenThrow(new DateTimeParseException("test", "t", 1));

        var model = new ConcurrentModel();
        var view = controller.sessionTimes(model);

        assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
        assertThat(view).isEqualTo("errors/404");

    }

    @Test
    public void whenSessionTimesSuccess() {
        List<LocalDateTime> test = List.of(LocalDateTime.now());
        List<TimeDto> dtoList = test.stream()
                .map(time -> new TimeDto(
                        time,
                        time.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))
                ))
                .toList();

        when(sessionService.getUniqueSessionTimesOnNextWeek()).thenReturn(test);

        var model = new ConcurrentModel();
        var view = controller.sessionTimes(model);

        assertThat(model.getAttribute("sessionTimes")).isEqualTo(dtoList);
        assertThat(view).isEqualTo("cinema/schedule/start_times");
    }

    @Test
    public void whenSessionTimesIsEmptyThenGetException() {
        when(sessionService.getUniqueSessionTimesOnNextWeek()).thenThrow(new NotFoundException("Нет сессий с фильмами на этой неделе"));

        var model = new ConcurrentModel();
        var view = controller.sessionTimes(model);

        assertThat(model.getAttribute("error")).isEqualTo("Нет сессий с фильмами на этой неделе");
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void sessionsByTimeIsSuccessThenReturnSessions() {
        var startTime = LocalDateTime.of(2025, 5, 26, 10, 0);
        String startTimeRaw = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<SessionDto> dtoList = List.of(new SessionDto(), new SessionDto());
        when(sessionService.getSessionsByStartTime(startTime)).thenReturn(dtoList);

        var model = new ConcurrentModel();
        var view = controller.sessionsByTime(startTimeRaw, model);

        assertThat(view).isEqualTo("cinema/schedule/sessions_by_time");
        assertThat(model.getAttribute("sessionsList")).isEqualTo(dtoList);
        assertThat(model.getAttribute("time")).isEqualTo(startTime);
    }

    @Test
    public void whenSessionsByTimeIsFailedBuyNotFoundThenGetException() {
        var startTime = LocalDateTime.of(2025, 5, 26, 10, 0);
        String startTimeRaw = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        when(sessionService.getSessionsByStartTime(startTime)).thenThrow(new NotFoundException("Не было найдено сессий с временем начала: " + startTime));

        var model = new ConcurrentModel();
        var view = controller.sessionsByTime(startTimeRaw, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Не было найдено сессий с временем начала: " + startTime);
    }

    @Test
    public void whenSessionsByTimeIsFailedByeGlobalExceptionThenGetException() {
        var startTime = LocalDateTime.of(2025, 5, 26, 10, 0);
        String startTimeRaw = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        when(sessionService.getSessionsByStartTime(startTime)).thenThrow(new RuntimeException("ошибка"));

        var model = new ConcurrentModel();
        var view = controller.sessionsByTime(startTimeRaw, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
    }

    @Test
    public void whenSelectSessionIsSuccessThenReturnSession() {
        int id = 1;
        SessionDto dto = new SessionDto();
        when(sessionService.getSessionById(id)).thenReturn(dto);

        var model = new ConcurrentModel();
        var view = controller.selectSession(id, model);

        assertThat(view).isEqualTo("cinema/selectSession");
        assertThat(model.getAttribute("selectedSession")).isEqualTo(dto);
    }

    @Test
    public void whenSelectSessionIsFailedThenGetException() {
        int id = 1;
        when(sessionService.getSessionById(id)).thenThrow(new NotFoundException("Не было найдено сессий с id: " + id));

        var model = new ConcurrentModel();
        var view = controller.selectSession(id, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Не было найдено сессий с id: " + id);
    }

    @Test
    public void whenSelectSessionIsFailedByGlobalExceptionThenGetException() {
        int id = 1;
        when(sessionService.getSessionById(id)).thenThrow(new RuntimeException("ошибка"));

        var model = new ConcurrentModel();
        var view = controller.selectSession(id, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
    }

    @Test
    void whenSessionsByTimeWithInvalidStartTimeThenReturnsErrorPage() {
        var model = new ConcurrentModel();
        var view = controller.sessionsByTime("invalid", model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
    }
}