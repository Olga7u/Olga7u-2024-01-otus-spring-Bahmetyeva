package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable("id") long id) {
        BookDto bookDto = bookService.findBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @PostMapping("/api/books")
    public ResponseEntity<Book> saveBook(@RequestBody BookDto bookDto) {
        Book book = bookService.saveBook(bookDto);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/api/books/{id}")
    public void deleteById(@PathVariable("id") long id) {
        bookService.deleteById(id);
    }
}
