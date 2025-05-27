package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.Converter;
import org.sql2o.converters.ConverterException;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.Quirks;
import ru.job4j.cinema.dto.SessionDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionRepositoryTest {
    private static Sql2o sql2o;
    private static SessionRepository sessionRepository;
    private final int MAIN_ID_FOR_TEST = 999;
    private final int FILM_ID_FOR_TEST = 1000;
    private final int MAIN_HALLS_ID_FOR_TEST = 1001;
    private final LocalDateTime MAIN_LOCAL_TIME_START_FOR_TEST = LocalDateTime.of(2025, 6, 1, 14, 0);
    private final LocalDateTime MAIN_LOCAL_TIME_END_FOR_TEST = LocalDateTime.of(2025, 6, 1, 16, 0);
    private final int MAIN_PRICE_FOR_TEST = 333;

    private final String HALL_NAME_FOR_TEST = "TestHall";
    private final String DESCRIPTION_FOR_TEST = "Test description";
    private final int ROW_COUNT_FOR_TEST = 15;
    private final int PLACE_COUNT_FOR_TEST = 25;

    private final String NAME_FOR_TEST = "Test name for film";
    private final int YEAR_FOR_TEST = 2002;
    private final int GENRE_ID_FOR_TEST = 132;
    private final int MINIMAL_AGE_FOR_TEST = 20;
    private final int DURATION_IN_MINUTES_FOR_TEST = 234;
    private final int FILE_ID_FOR_TEST = 999;

    private final String PATH_FOR_TEST = "testPath";


    @BeforeAll
    public static void setUp() throws IOException {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream("db/liquibase_test.properties")) {
            properties.load(in);
        }

        // Создаем DataSource (как в DatasourceConfiguration)
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));

        // Создаем Sql2o с конвертерами (как в DatasourceConfiguration)
        sql2o = new Sql2o(dataSource, createConverters());  // !!!
        sessionRepository = new SessionRepository(sql2o);
    }

    private static Quirks createConverters() {
        return new NoQuirks() {
            {
                converters.put(LocalDateTime.class, new Converter<LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(Object value) throws ConverterException {
                        if (value == null) return null;
                        if (!(value instanceof Timestamp)) throw new ConverterException("Invalid value to convert");
                        return ((Timestamp) value).toLocalDateTime();
                    }

                    @Override
                    public Object toDatabaseParam(LocalDateTime value) {
                        return value == null ? null : Timestamp.valueOf(value);
                    }
                });
            }
        };
    }

    @BeforeEach
    public void insertTestDate() {
        try (Connection conn = sql2o.open()) {
            clearTestData();
            conn.createQuery("""
                            insert into genres(id, name) values (:id, :name);
                            """)
                    .addParameter("id", GENRE_ID_FOR_TEST)
                    .addParameter("name", NAME_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("""
                            insert into files(id, name, path) values (:id, :name, :path);
                            """)
                    .addParameter("id", FILE_ID_FOR_TEST)
                    .addParameter("name", NAME_FOR_TEST)
                    .addParameter("path", PATH_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("""
                            insert into films (id, name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                            values(:id, :name, :description, :year, :genreId, :minimalAge, :durationInMinutes, :fileId)
                            """)
                    .addParameter("id", FILM_ID_FOR_TEST)
                    .addParameter("name", NAME_FOR_TEST)
                    .addParameter("description", DESCRIPTION_FOR_TEST)
                    .addParameter("year", YEAR_FOR_TEST)
                    .addParameter("genreId", GENRE_ID_FOR_TEST)
                    .addParameter("minimalAge", MINIMAL_AGE_FOR_TEST)
                    .addParameter("durationInMinutes", DURATION_IN_MINUTES_FOR_TEST)
                    .addParameter("fileId", FILE_ID_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("""
                            insert into halls(id, name, row_count, place_count, description)
                            values (:id, :name, :rowCount, :placeCount, :description);
                            """)
                    .addParameter("id", MAIN_HALLS_ID_FOR_TEST)
                    .addParameter("name", HALL_NAME_FOR_TEST)
                    .addParameter("rowCount", ROW_COUNT_FOR_TEST)
                    .addParameter("placeCount", PLACE_COUNT_FOR_TEST)
                    .addParameter("description", DESCRIPTION_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("""
                            insert into film_sessions(id, film_id, halls_id, start_time, end_time, price)
                            values(:id, :filmId, :hallsId, :start, :end, :price);
                            """)
                    .addParameter("id", MAIN_ID_FOR_TEST)
                    .addParameter("filmId", FILM_ID_FOR_TEST)
                    .addParameter("hallsId", MAIN_HALLS_ID_FOR_TEST)
                    .addParameter("start", MAIN_LOCAL_TIME_START_FOR_TEST)
                    .addParameter("end", MAIN_LOCAL_TIME_END_FOR_TEST)
                    .addParameter("price", MAIN_PRICE_FOR_TEST)
                    .executeUpdate();
        }
    }

    @AfterEach
    public void cleanUp() {
        clearTestData();
    }

    @Test
    public void whenGetSessionsByFilmIdIsSuccessfulThenReturnSessions() {
        List<SessionDto> sessions = sessionRepository.getSessionsByFilmId(FILM_ID_FOR_TEST);

        assertThat(sessions).hasSize(1);
        SessionDto session = sessions.get(0);

        assertThat(session.getId()).isEqualTo(MAIN_ID_FOR_TEST);
        assertThat(session.getFilmId()).isEqualTo(FILM_ID_FOR_TEST);
        assertThat(session.getHallName()).isEqualTo(HALL_NAME_FOR_TEST);
        assertThat(session.getRowCount()).isEqualTo(ROW_COUNT_FOR_TEST);
        assertThat(session.getPlaceCount()).isEqualTo(PLACE_COUNT_FOR_TEST);
        assertThat(session.getStartTime()).isEqualTo(MAIN_LOCAL_TIME_START_FOR_TEST);
        assertThat(session.getEndTime()).isEqualTo(MAIN_LOCAL_TIME_END_FOR_TEST);
        assertThat(session.getPrice()).isEqualTo(MAIN_PRICE_FOR_TEST);
    }

    @Test
    public void whenGetSessionsByFilmIdIsFailedBecauseOfBadParamThenReturnEmptyList() {
        List<SessionDto> sessions = sessionRepository.getSessionsByFilmId(21321312);

        assertThat(sessions).isEmpty();
    }

    @Test
    public void whenGetSessionByIdIsSuccessfulThenReturnSessionDto() {
        Optional<SessionDto> sessionDto = sessionRepository.getSessionById(MAIN_ID_FOR_TEST);

        assertThat(sessionDto).isNotEmpty();
        SessionDto session = sessionDto.get();

        assertThat(session.getId()).isEqualTo(MAIN_ID_FOR_TEST);
        assertThat(session.getFilmId()).isEqualTo(FILM_ID_FOR_TEST);
        assertThat(session.getHallName()).isEqualTo(HALL_NAME_FOR_TEST);
        assertThat(session.getRowCount()).isEqualTo(ROW_COUNT_FOR_TEST);
        assertThat(session.getPlaceCount()).isEqualTo(PLACE_COUNT_FOR_TEST);
        assertThat(session.getStartTime()).isEqualTo(MAIN_LOCAL_TIME_START_FOR_TEST);
        assertThat(session.getEndTime()).isEqualTo(MAIN_LOCAL_TIME_END_FOR_TEST);
        assertThat(session.getPrice()).isEqualTo(MAIN_PRICE_FOR_TEST);
    }

    @Test
    public void whenGetSessionByIdIsFailedBecauseOfBadParamThenReturnEmptySessionDto() {
        Optional<SessionDto> sessionDto = sessionRepository.getSessionById(3213123);

        assertThat(sessionDto).isEmpty();
    }

    @Test
    public void whenGetSessionTimesOnNextWeekIsSuccessfulThenReturnSessions() {
        List<LocalDateTime> sessions = sessionRepository.getSessionTimesOnNextWeek();

        assertThat(sessions).isNotEmpty();
    }

    @Test
    public void whenGetSessionTimesOnNextWeekIsSuccessfulThenReturnListOfSessions() {
        List<SessionDto> sessions = sessionRepository.getSessionsByStartTime(MAIN_LOCAL_TIME_START_FOR_TEST);

        assertThat(sessions).isNotEmpty();
    }

    @Test
    public void whenGetSessionTimesOnNextWeekIsFailedBecauseOfBadParamThenReturnEmptyListOfSessions() {
        List<SessionDto> sessions = sessionRepository.getSessionsByStartTime(LocalDateTime.of(1900, 11, 21, 12, 2));

        assertThat(sessions).isEmpty();
    }


    private void clearTestData() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from film_sessions where id = :id")
                    .addParameter("id", MAIN_ID_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("delete from films where id = :id")
                    .addParameter("id", FILM_ID_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("delete from files where id = :id")
                    .addParameter("id", FILE_ID_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("delete from genres where id = :id")
                    .addParameter("id", GENRE_ID_FOR_TEST)
                    .executeUpdate();

            conn.createQuery("delete from halls where id = :id")
                    .addParameter("id", MAIN_HALLS_ID_FOR_TEST)
                    .executeUpdate();
        }
    }
}