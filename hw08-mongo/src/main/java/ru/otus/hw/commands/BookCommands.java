package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.services.BookService;

import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return bookService.findAllAuthors();
    }

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll();
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id);
    }

    // bins newBook newAuthor 1,6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorName,  List<String> genreList) {
        return bookService.insert(title, authorName,  genreList);
    }

    // bupd 4 editedBook newAuthor 2,5
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorName,  List<String> genreList) {
        return bookService.update(id, title, authorName,  genreList);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }

    // cins 1, bla
    @ShellMethod(value = "Insert comment on book", key = "cins")
    public String insertComment(String bookId, String text) {
        return bookService.insertComment(bookId, text);
    }
}
