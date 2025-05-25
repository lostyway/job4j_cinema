package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
public class TicketRepository implements ITickerRepository {
    private final Sql2o sql2o;

    public TicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<TicketDto> buyTicket(Ticket ticket) {
        var sql = """
                insert into tickets(session_id, row_number, place_number, user_id)
                values(:session_id, :row_number, :place_number, :user_id)
                """;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(sql, true)
                    .addParameter("session_id", ticket.getSessionId())
                    .addParameter("row_number", ticket.getRowNumber())
                    .addParameter("place_number", ticket.getPlaceNumber())
                    .addParameter("user_id", ticket.getUserId());
            int generateId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generateId);
            return getTicketDtoById(generateId);
        }
    }

    @Override
    public Optional<TicketDto> getTicketDtoById(int id) {
        var sql = """
                select ticket.id as id,
                       ticket.row_number as rowNumber,
                       ticket.place_number as placeNumber,
                       us.email as email,
                       session.start_time as startTime,
                       session.end_time as endTime,
                       film.name as filmName,
                       hall.name as hallName,
                       session.price as price
                from tickets ticket
                join users us on us.id = ticket.user_id
                join film_sessions session on session.id = ticket.session_id
                join films film on session.film_id = film.id
                join halls hall on session.halls_id = hall.id
                where ticket.id = :id
                """;
        try (var connection = sql2o.open()) {
            return Optional.ofNullable(connection.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(TicketDto.class));
        }
    }
}
