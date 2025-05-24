package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;

public interface ITicketService {
    TicketDto buyTicket(Ticket ticket);

    TicketDto getTicketDtoById(int id);
}
