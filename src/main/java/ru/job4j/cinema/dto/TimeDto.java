package ru.job4j.cinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeDto timeDto = (TimeDto) o;
        return Objects.equals(dateTime, timeDto.dateTime)
                && Objects.equals(displayTime, timeDto.displayTime)
                && Objects.equals(urlTime, timeDto.urlTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, displayTime, urlTime);
    }
}
