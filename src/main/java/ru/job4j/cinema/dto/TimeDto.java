package ru.job4j.cinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TimeDto {
    private LocalDateTime dateTime;
    private String displayTime;
    private String urlTime;

    public TimeDto(LocalDateTime dateTime, String displayTime) {
        this.dateTime = dateTime;
        this.displayTime = displayTime;
        this.urlTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
