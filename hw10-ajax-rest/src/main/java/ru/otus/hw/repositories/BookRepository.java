package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"genres", "author"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"genres", "author"})
    Optional<Book> findById(long id);

    @Modifying
    @Query(value = "INSERT INTO books (id, title, author_id) VALUES (:id, :title, :authorId)", nativeQuery = true)
    void saveBook(@Param("id") long id, @Param("title") String title, @Param("authorId") long authorId);

    @Modifying
    @Query(value = "INSERT INTO books_genres (book_id, genre_id) VALUES (:bookId, :genreId) ", nativeQuery = true)
    void addGenreToBook(@Param("bookId") long bookId, @Param("genreId") long genreId);

    @Query(value = "CALL NEXTVAL('SEQ_ID')", nativeQuery = true)
    Long getLastInsertedBookId();
}
