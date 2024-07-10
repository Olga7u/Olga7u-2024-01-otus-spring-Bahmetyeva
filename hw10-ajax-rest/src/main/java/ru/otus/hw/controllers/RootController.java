package ru.otus.hw.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RootController {

    @ExceptionHandler(Throwable.class)
    public String handleException(Throwable exception, HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String params = request.getQueryString();
        log.error("Internal error on request " + method + ": " + url + "?" + params, exception);
        return exception.getMessage();
    }
}
