package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    @Autowired
    JpaCommentRepository jpaCommentRepository;

    @DisplayName("должен загружать комменты по id книги")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findAllCommentsByBookId(Book book) {
        List<Comment> actualComments = jpaCommentRepository.findAllByBookId(book.getId());
        List<Comment> expectedComments = getDbCommentsByBookId(book.getId());

        assertThat(actualComments).isNotNull().hasSize(expectedComments.size());
        expectedComments.forEach(b -> assertThat(actualComments.contains(b)));
    }

    @DisplayName("должен загружать коммент по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void findById(Comment expected) {
        Optional<Comment> actual = jpaCommentRepository.findById(expected.getId());

        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expected);
        assertThat(actual.get().getBook().getId()).isEqualTo(expected.getBook().getId());
    }

    @Test
    void insert() {
        Comment expected = new Comment(0, getDbBooks().get(0), "Text_new");
        Comment actual = jpaCommentRepository.insert(expected);

        assertThat(actual).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);

        assertThat(jpaCommentRepository.findById(actual.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(actual);

    }

    @Test
    void deleteById() {
        assertThat(jpaCommentRepository.findById(1L)).isPresent();
        jpaCommentRepository.deleteById(1L);
        assertThat(jpaCommentRepository.findById(1L)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    private static List<Comment> getDbComments() {
        List<Book> dbBooks = getDbBooks();

        return IntStream.range(1, 7).boxed()
                .map(id -> new Comment(id, dbBooks.get((id - 1) / 2), "Text_" + id)).toList();
    }

    private static List<Comment> getDbCommentsByBookId(long bookId) {
        return getDbComments().subList((int) (bookId - 1) * 2, (int) ((bookId - 1) * 2 + 2));
    }
}