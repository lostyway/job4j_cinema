package ru.job4j.cinema.dto;

import java.util.Objects;

public class FilmDto {

    private int id;
    private String name;
    private String description;
    private int year;
    private int minimalAge;
    private int durationInMinutes;
    private String genre;

    public FilmDto() {
    }

    public FilmDto(int id, String name, String description, int year, int minimalAge, int durationInMinutes, String genre) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.minimalAge = minimalAge;
        this.durationInMinutes = durationInMinutes;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilmDto filmDto = (FilmDto) o;
        return id == filmDto.id && year == filmDto.year && Objects.equals(name, filmDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year);
    }
}
