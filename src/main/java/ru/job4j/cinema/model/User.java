package ru.job4j.cinema.model;

import java.util.Map;

public class User {
    public static final Map<String, String> MAP_BASE = Map.of(
            "id", "id",
            "full_name", "name",
            "email", "email",
            "password", "password"
    );

    private int id;
    private String name;
    private String email;
    private String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}