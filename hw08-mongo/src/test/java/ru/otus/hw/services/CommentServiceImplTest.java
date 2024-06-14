package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.listeners.CommentDeleteListener;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import({CommentServiceImpl.class, CommentDeleteListener.class})
class CommentServiceImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentServiceImpl commentService;

    @Autowired
    CommentDeleteListener commentDeleteListener;

    private final String bookId = "10";

    private final String commentID = "12";

    private final String commentText = "Text2";

    private final Comment commentToDelete = new Comment(commentID, commentText, bookId);

    @BeforeEach
    void setUp() {
        Comment newComment1 = commentRepository.save(new Comment("11", "Text1", bookId));
        Comment newComment2 = commentRepository.save(commentToDelete);

        Book book = new Book(bookId, "Book1");
        book.setComments(List.of(newComment1, newComment2));

        bookRepository.save(book);
    }

    @Test
    void deleteById() {
        commentService.deleteById(commentID);

        Comment retrievedComment = commentRepository.findById(commentID).orElse(null);
        assertNull(retrievedComment);

        Book book = bookRepository.findById(bookId).orElse(null);
        assertNotNull(book);

        List<Comment> retrievedComments = book.getComments();
        assertFalse(retrievedComments.contains(commentToDelete));
    }
}