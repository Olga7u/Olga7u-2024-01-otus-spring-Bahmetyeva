package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;

    @NotBlank(message = "Title should not be blank")
    private String title;

    private Author author;

    private Set<Genre> genres;

    private List<Comment> comments;

    public Book toDomainObject() {
        if (comments == null) {
            comments = List.of();
        }
        return new Book(id, title, author, genres, comments);
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres(), book.getComments());
    }

    public String genresToString() {
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));
    }
}
