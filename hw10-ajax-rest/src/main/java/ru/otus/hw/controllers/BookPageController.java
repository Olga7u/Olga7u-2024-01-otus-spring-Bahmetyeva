package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookPageController {

    @GetMapping("/")
    public String listBooksPage() {
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editBookPage() {
        return "edit";
    }
}
