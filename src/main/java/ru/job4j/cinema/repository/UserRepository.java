package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {
    private final Sql2o sql2o;

    public UserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        var sql = "INSERT INTO users (full_name, email, password) VALUES (:name, :email, :password)";
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(sql)
                    .addParameter("name", user.getName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        var sql = "SELECT * FROM users WHERE email = :email AND password = :password";
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            var result = query.setColumnMappings(User.MAP_BASE).executeAndFetchFirst(User.class);
            return Optional.ofNullable(result);
        }
    }
}
