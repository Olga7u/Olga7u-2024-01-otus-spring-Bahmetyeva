package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private Author author;

    private Set<Genre> genres;

    public Book toDomainObject() {

        return new Book(id, title, author, genres);
    }

    public static BookDto fromDomainObject(Book book) {

        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres());
    }

    public String genresToString() {
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));
    }

    public List<Long> genreIds() {
        return genres.stream()
                .map(Genre::getId)
                .toList();
    }
}
