package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(Book book) {

        String genresString = "";
        if (book.getGenres() != null) {
            genresString = book.getGenres().stream()
                    .map(genreConverter::genreToString)
                    .map("{%s}"::formatted)
                    .collect(Collectors.joining(", "));
        }

        String result = "Id: %s, title: %s, author: {%s}, genres: [%s]";

        return result.formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genresString);
    }

    public String bookToStringWithComments(Book book) {
        String result = bookToString(book) + "\nComments:\n";

        String comments = "";
        if (book.getComments() != null && !book.getComments().isEmpty()) {
            comments = book.getComments().stream()
                    .map(commentConverter::commentToString)
                    .map("{%s}"::formatted)
                    .collect(Collectors.joining("\n"));
        }
        return result + comments;
    }
}
