package ru.job4j.cinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TicketDto {
    private int id;
    private int sessionId;
    private int rowNumber;
    private int placeNumber;
    private String email;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String filmName;
    private String hallName;
    private int price;

    public TicketDto() {
    }

    public TicketDto(int id, int sessionId, int rowNumber, int placeNumber, String email, LocalDateTime startTime, LocalDateTime endTime, String filmName, String hallName, int price) {
        this.id = id;
        this.sessionId = sessionId;
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
        this.email = email;
        this.startTime = startTime;
        this.endTime = endTime;
        this.filmName = filmName;
        this.hallName = hallName;
        this.price = price;
    }
}
