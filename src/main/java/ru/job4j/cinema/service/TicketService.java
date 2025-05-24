package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDto;
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
                .orElseThrow(() -> new RuntimeException("Ticket is busy"));
    }

    @Override
    public TicketDto getTicketDtoById(int id) {
        return ticketRepository.getTicketDtoById(id)
                .orElseThrow(() -> new RuntimeException("No ticket found"));
    }
}
