package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    @DisplayName("список всех жанров")
    @Test
    void findAll() {
        List<Genre> actual = jpaGenreRepository.findAll();

        assertThat(actual).containsExactlyElementsOf(dbGenres);
        actual.forEach(System.out::println);
    }

    @DisplayName("список жанров по заданным идентификаторам")
    @Test
    void findAllByIds() {
        Set<Genre> expected = new HashSet<>(dbGenres.subList(1, 4));
        Set<Genre> actual = jpaGenreRepository.findAllByIds(Set.of(2L, 3L, 4L));

        assertThat(actual).containsExactlyElementsOf(expected);
        actual.forEach(System.out::println);
    }
}