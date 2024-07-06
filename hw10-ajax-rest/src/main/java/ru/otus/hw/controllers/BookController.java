package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public List<BookDto> getAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping(value = "/books/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable("id") long id) {
        try {
            BookDto bookDto = bookService.findBookById(id);
            return new ResponseEntity<>(bookDto, HttpStatusCode.valueOf(200));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> saveBook(@RequestBody BookDto bookDto) {
        try {
            Book book = bookService.saveBook(bookDto);
            return new ResponseEntity<>(book, HttpStatusCode.valueOf(200));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @DeleteMapping("/books/{id}")
    public void deleteById(@PathVariable("id") long id) {
        try {
            bookService.deleteById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }
}
