package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

@Controller
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buyTicket")
    public String buyTicket(@RequestParam int sessionId,
                            @RequestParam int rowNumber,
                            @RequestParam int placeNumber,
                            RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setSessionId(sessionId);
            ticket.setRowNumber(rowNumber);
            ticket.setPlaceNumber(placeNumber);

            TicketDto ticketToBuy = ticketService.buyTicket(ticket);
            redirectAttributes.addAttribute("ticket", ticketToBuy);
            return "redirect:/users/home";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Билет уже куплен");
            return "errors/404";
        }
    }
}
