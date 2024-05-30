package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("список всех авторов")
    @Test
    void findAll() {
        List<Author> actual = jpaAuthorRepository.findAll();

        assertThat(actual).containsExactlyElementsOf(dbAuthors);
        actual.forEach(System.out::println);
    }

    @DisplayName("автор по ID")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void findById(Author expectedAuthor) {
        Optional<Author> actualAuthor = jpaAuthorRepository.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent().get().isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}