package ru.job4j.cinema.repository;

import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

public interface ITickerRepository {
    Optional<TicketDto> buyTicket(Ticket ticket);

    Optional<TicketDto> getTicketDtoById(int id);
}
