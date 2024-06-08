package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll();
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id);
    }

    // bins newBook 1 1,6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        return bookService.insert(title, authorId, genresIds);
    }

    // bupd 4 editedBook 3 2,5
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds, List<String> commentTexts) {
        return bookService.update(id, title, authorId, genresIds, commentTexts);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }

    // cins 1, bla
    @ShellMethod(value = "Insert comment on book", key = "cins")
    public String insertComment(long bookId, String text) {
        return bookService.insertComment(bookId, text);
    }

    // bc 1
    @ShellMethod(value = "Find comments by book_id", key = "bc")
    public String findCommentsByBookId(long bookId) {
        return bookService.findComments(bookId);
    }
}
