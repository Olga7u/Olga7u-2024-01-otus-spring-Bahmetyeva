package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@Slf4j
public class RootController {

    @ExceptionHandler(Throwable.class)
    public String handleException(Throwable exception, ServletWebRequest request) {
        String method = request.getHttpMethod().name();
        String uri = request.getRequest().getRequestURI();
        String params = request.getRequest().getQueryString();
        log.error("Internal error on request " + method + ": " + uri + "?" + params, exception);
        return exception.getMessage();
    }
}
