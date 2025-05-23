package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IndexControllerTest {

    @Test
    public void whenGetMainPage() {
        IndexController controller = new IndexController();
        String view = controller.getIndex();
        assertEquals("index", view);
    }
}