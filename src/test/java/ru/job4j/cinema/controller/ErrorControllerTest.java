package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ErrorControllerTest {

    @Test
    public void whenGetError404WithErrorParam_thenReturnErrorPageWithCustomMessage() throws Exception {
        String errorMessage = "Тестовая ошибка";
        Model model = mock(Model.class);
        ErrorController errorController = new ErrorController();
        String viewName = errorController.error404(errorMessage, model);

        assertEquals("errors/404", viewName);

        ArgumentCaptor<String> attributeNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> attributeValueCaptor = ArgumentCaptor.forClass(String.class);
        verify(model).addAttribute(attributeNameCaptor.capture(), attributeValueCaptor.capture());

        assertEquals("error", attributeNameCaptor.getValue());
        assertEquals(errorMessage, attributeValueCaptor.getValue());
    }

    @Test
    public void whenGetError404WithOutErrorParam_thenReturnErrorPageWithDefaultMessage() throws Exception {
        Model model = mock(Model.class);
        ErrorController errorController = new ErrorController();
        String viewName = errorController.error404(null, model);

        assertEquals("errors/404", viewName);

        ArgumentCaptor<String> attributeNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> attributeValueCaptor = ArgumentCaptor.forClass(String.class);
        verify(model).addAttribute(attributeNameCaptor.capture(), attributeValueCaptor.capture());

        assertEquals("error", attributeNameCaptor.getValue());
        assertEquals("Страница не найдена", attributeValueCaptor.getValue());
    }
}