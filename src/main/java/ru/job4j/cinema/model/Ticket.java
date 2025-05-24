package ru.job4j.cinema.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Ticket {
    private int id;
    private int sessionId;
    private int rowNumber;
    private int placeNumber;
    private int userId;

    public Ticket() {
    }

    public Ticket(int id, int sessionId, int rowNumber, int placeNumber, int userId) {
        this.id = id;
        this.sessionId = sessionId;
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && sessionId == ticket.sessionId && rowNumber == ticket.rowNumber && placeNumber == ticket.placeNumber && userId == ticket.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, rowNumber, placeNumber, userId);
    }
}
