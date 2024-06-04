package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAll() {
        List<Book> actualBooks = jpaBookRepository.findAll();
        List<Book> expectedBooks = getDbBooks();

        assertThat(actualBooks).isNotNull().hasSize(expectedBooks.size());
        expectedBooks.forEach(b -> assertThat(actualBooks).contains(b));
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен добавлять коммент в книгу")
    @Test
    void insertComment(){
        long bookId = 1L;
        String text = "bla-bla";

        Book expected = entityManager.find(Book.class, bookId);
        expected.getComments().add(new Comment(7L, text));

        Book actual = jpaBookRepository.insertComment(bookId, text);
        assertThat(actual).isEqualTo(expected);
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> entityManager.find(Book.class, id))
                .toList();
    }
}