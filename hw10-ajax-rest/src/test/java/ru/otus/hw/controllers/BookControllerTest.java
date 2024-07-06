package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void getAllBooks() throws Exception {
        List<BookDto> books = List.of(
                new BookDto(1L, "Book_1", new Author(1L, "Author_1"),
                        Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"))),
                new BookDto(2L, "Book_2", new Author(2L, "Author_2"),
                        Set.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4")))
        );

        given(bookService.findAllBooks()).willReturn(books);

        List<BookDto> expectedResult = books.stream()
                .toList();

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    void getBookById() throws Exception {
        BookDto book = new BookDto(1L, "Book_1", new Author(1L, "Author_1"),
                Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        given(bookService.findBookById(1L)).willReturn(book);

        mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void saveBook() throws Exception {
        Book book = new Book(1L, "Book_1", new Author(1L, "Author_1"),
                Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        given(bookService.saveBook(any())).willReturn(book);
        String expectedResult = mapper.writeValueAsString(book);

        mvc.perform(post("/books")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1L);
    }

}