package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class TicketControllerTest {
    private TicketController controller;
    private TicketService ticketService;
    private HttpSession session;
    private TicketDto ticketDto;
    private Ticket ticket;
    private Model model;
    private RedirectAttributes redirectAttributes;
    private final int USER_ID = 10;

    @BeforeEach
    public void setUp() {
        ticketService = mock(TicketService.class);
        session = mock(HttpSession.class);
        ticketDto = new TicketDto();
        ticket = new Ticket();
        model = new ConcurrentModel();
        controller = new TicketController(ticketService);
        redirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    public void whenGetTicket() {
        var view = controller.getTicket();
        assertThat(view).isEqualTo("ticket");
    }

    @Test
    public void whenBuyTicketIsSuccessThanRedirectToTicketAndGetTicket() {
        when(ticketService.buyTicket(any(Ticket.class))).thenReturn(ticketDto);
        when(session.getAttribute("userId")).thenReturn(USER_ID);
        int sessionId = 1;
        int rowNumber = 5;
        int placeNumber = 10;

        var view = controller.buyTicket(sessionId, rowNumber, placeNumber, redirectAttributes, session, model);

        verify(redirectAttributes).addFlashAttribute("ticket", ticketDto);
        assertThat(view).isEqualTo("redirect:/ticket");
    }

    @Test
    public void whenBuyTicketIsFailedByGlobalExceptionThanGetExceptionAngErrorView() {
        when(ticketService.buyTicket(any(Ticket.class))).thenThrow(new RuntimeException());
        when(session.getAttribute("userId")).thenReturn(USER_ID);
        int sessionId = 1;
        int rowNumber = 5;
        int placeNumber = 10;

        var view = controller.buyTicket(sessionId, rowNumber, placeNumber, redirectAttributes, session, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Произошла непредвиденная ошибка. Попробуйте позже");
    }

    @Test
    public void whenBuyTicketIsFailedByNotFoundExceptionThanGetExceptionAngErrorView() {
        when(ticketService.buyTicket(any(Ticket.class))).thenThrow(new NotFoundException("Билет уже был куплен или вы не были зарегистрированы для покупки"));
        when(session.getAttribute("userId")).thenReturn(USER_ID);
        int sessionId = 1;
        int rowNumber = 5;
        int placeNumber = 10;

        var view = controller.buyTicket(sessionId, rowNumber, placeNumber, redirectAttributes, session, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("error")).isEqualTo("Билет уже был куплен или вы не были зарегистрированы для покупки");
    }
}