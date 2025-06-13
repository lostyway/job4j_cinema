package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.cinema.exceptions.NotFoundException;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalControllerHandler {

    @ExceptionHandler(value = {NotFoundException.class, IllegalArgumentException.class})
    public ModelAndView handleNotFoundException(NotFoundException e) {
        log.warn("NotFoundException: {}", e.getMessage());
        ModelAndView modelAndView = new ModelAndView("errors/404");
        modelAndView.addObject("error", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        log.error("Необработанное исключение", e);
        ModelAndView modelAndView = new ModelAndView("errors/404");
        modelAndView.addObject("error", "Произошла непредвиденная ошибка. Попробуйте позже");
        return modelAndView;
    }
}
