package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

public interface IUserService {
    User save(User user);

    User findUserByEmailAndPassword(String email, String password);
}
