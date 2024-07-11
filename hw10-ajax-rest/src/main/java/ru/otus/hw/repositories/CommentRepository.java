package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c.id, c.text from comments c where c.book_id = :bookId", nativeQuery = true)
    List<Comment> findAllByBookId(@Param("bookId") long bookId);

    @Modifying
    @Query(value = "insert into comments(book_id, text) values(:bookId, :text)", nativeQuery = true)
    void insertComment(@Param("bookId") long bookId, @Param("text") String text);
}
