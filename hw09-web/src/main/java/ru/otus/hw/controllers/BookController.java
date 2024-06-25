package ru.otus.hw.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtoList = books.stream().map(BookDto::fromDomainObject).toList();
        model.addAttribute("books", bookDtoList);
        return "booklist";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable long id, Model model) {

        Book book = new Book();
        if (id != 0) {
            book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        }

        List<Author> authors = authorRepository.findAll();
        List<Genre> genres = genreRepository.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Author> authors = authorRepository.findAll();
            List<Genre> genres = genreRepository.findAll();

            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "edit";
        }

        Book book = bookDto.toDomainObject();
        bookRepository.save(book);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        model.addAttribute("book", book);
        return "delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@ModelAttribute("book") BookDto bookDto) {
        bookRepository.delete(bookDto.toDomainObject());
        return "redirect:/";
    }

}
