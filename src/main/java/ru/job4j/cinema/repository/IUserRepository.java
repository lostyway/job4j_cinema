package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> save(User user);

    Optional<User> findUserByEmailAndPassword(String email, String password);
}
