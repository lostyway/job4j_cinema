package ru.job4j.cinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Getter
public class SessionDto {

    private int id;
    private int filmId;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
    private int rowCount;
    private int placeCount;
    private List<Integer> rowsList;
    private List<Integer> placeList;
    private List<FilmDto> filmList;

    public SessionDto() {
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public SessionDto(int id, int filmId, String hallName, LocalDateTime startTime, LocalDateTime endTime, int price, int rowCount, int placeCount) {
        this.id = id;
        this.filmId = filmId;
        this.hallName = hallName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.rowCount = rowCount;
        this.placeCount = placeCount;
    }

    public void fillRowsAndCount() {
        this.rowsList = IntStream.rangeClosed(1, rowCount).boxed().collect(Collectors.toList());
        this.placeList = IntStream.rangeClosed(1, placeCount).boxed().collect(Collectors.toList());
    }
}
