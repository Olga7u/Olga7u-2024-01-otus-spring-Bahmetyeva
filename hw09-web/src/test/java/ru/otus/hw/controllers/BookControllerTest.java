package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void listPage() throws Exception {
        List<Book> books = List.of(
                new Book(1L, "Book_1", new Author(1L, "Author_1"),
                        Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")),
                        List.of(new Comment(1L, "Text_1"))),
                new Book(2L, "Book_2", new Author(2L, "Author_2"),
                        Set.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4")),
                        List.of(new Comment(2L, "Text_2")))
        );

        given(bookRepository.findAll()).willReturn(books);

        ResultActions resultActions = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("booklist"));

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        checkBooks(books, content);
    }

    @Test
    void editPage() throws Exception {
        Book book = new Book(1L, "Book_1", new Author(1L, "Author_1"),
                Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")),
                List.of(new Comment(1L, "Text_1")));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        ResultActions resultActions = mvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ;

        Document doc = Jsoup.parse(content);
        assertEquals(String.valueOf(book.getId()), doc.select("#id-input").first().val());
        assertEquals(book.getTitle(), doc.select("#book-title-input").first().val());
    }

    @Test
    void saveBook() throws Exception {
        Book book = new Book(1L, "Book_1", new Author(1L, "Author_1"),
                Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")),
                List.of(new Comment(1L, "Text_1")));
        given(bookRepository.save(any())).willReturn(book);
        mvc.perform(post("/edit/1").flashAttr("book", BookDto.fromDomainObject(book)))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    private void checkBooks(List<Book> books, String content) {
        List<BookDto> bookDtos = books.stream().map(BookDto::fromDomainObject).toList();

        Document doc = Jsoup.parse(content);
        Elements tables = doc.select("table.books");
        assertFalse(tables.isEmpty());
        Element table = tables.first();

        Elements rows = table.select("tbody tr");
        assertEquals(bookDtos.size(), rows.size());

        for (int i = 0; i < bookDtos.size(); i++) {
            Element row = rows.get(i);
            Element idCell = row.select("td").get(0);
            Element titleCell = row.select("td").get(1);
            Element authorCell = row.select("td").get(2);
            Element genresCell = row.select("td").get(3);

            assertEquals(String.valueOf(bookDtos.get(i).getId()), idCell.text());
            assertEquals(bookDtos.get(i).getTitle(), titleCell.text());
            assertEquals(bookDtos.get(i).getAuthor().getFullName(), authorCell.text());
            assertEquals(bookDtos.get(i).genresToString(), genresCell.text());
        }
    }
}