package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.exceptions.NotFoundException;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Пользователь с почтой %s уже существует", user.getEmail())));
    }

    @Override
    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundException("Почта или пароль введены неверно"));
    }
}
