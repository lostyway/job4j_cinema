package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
            //TODO Вернуть как было после регистрации пользователя
            //Integer userId = (Integer) session.getAttribute("userId");
            Ticket ticket = new Ticket();
            ticket.setUserId(1);
            ticket.setSessionId(sessionId);
            ticket.setRowNumber(rowNumber);
            ticket.setPlaceNumber(placeNumber);

            TicketDto ticketToBuy = ticketService.buyTicket(ticket);
            redirectAttributes.addFlashAttribute("ticket", ticketToBuy);
            return "redirect:/ticket";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Билет уже был куплен или вы не были зарегистрированы для покупки");
            return "errors/404";
        }
    }

    @GetMapping("/ticket")
    public String getTicket() {
        return "ticket";
    }
}
