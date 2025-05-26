package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

@Service
public class TicketService implements ITicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketDto buyTicket(Ticket ticket) {
        return ticketRepository.buyTicket(ticket)
                .orElseThrow(() -> new NotFoundException("Билет уже был куплен или вы не были зарегистрированы для покупки"));
    }
}
