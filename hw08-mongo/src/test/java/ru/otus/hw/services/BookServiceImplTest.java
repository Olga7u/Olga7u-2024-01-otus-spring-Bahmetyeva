package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.listeners.BookDeleteListener;
import ru.otus.hw.listeners.CommentDeleteListener;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import javax.management.Query;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import({BookServiceImpl.class, BookDeleteListener.class})
class BookServiceImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookDeleteListener bookDeleteListener;

    @MockBean
    private BookConverter bookConverter;

    private final String bookId = "10";

    private final String bookName = "Book1";

    private final String commentText = "Text1";

    @BeforeEach
    void setUp() {
        when(bookConverter.bookToStringWithComments(any(Book.class)))
                .thenReturn("Any book");
    }

    @DisplayName("Insert comment and find in db")
    @Test
    public void testInsertCommentAndFind() {
        Book book = new Book(bookId, bookName);
        bookRepository.save(book);

        bookService.insertComment(bookId, commentText);

        List<Comment> retrievedComments = getComments();

        assertNotNull(retrievedComments);
        assertEquals(1, retrievedComments.size());

        assertEquals(commentText, retrievedComments.get(0).getText());
        assertEquals(bookId, retrievedComments.get(0).getBookId());

        String commentId = retrievedComments.get(0).getId();

        Book retrievedBook = bookRepository.findById(bookId).orElse(null);
        assertNotNull(retrievedBook);

        retrievedComments = retrievedBook.getComments();

        assertEquals(1, retrievedComments.size());
        assertEquals(commentId, retrievedComments.get(0).getId());
    }

    @DisplayName("Delete book with all comments")
    @Test
    void deleteById() {
        Comment newComment = commentRepository.save(new Comment(commentText, bookId));

        Book book = new Book(bookId, bookName);

        book.setComments(List.of(newComment));
        bookRepository.save(book);

        bookService.deleteById(bookId);

        Book retrievedBook = bookRepository.findById(bookId).orElse(null);
        assertNull(retrievedBook);

        List<Comment> retrievedComments = getComments();
        assertNotNull(retrievedComments);
        assertEquals(0, retrievedComments.size());
    }


    private List<Comment> getComments(){
        Comment commentEx = new Comment(commentText, bookId);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("bookId", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Comment> example = Example.of(commentEx, matcher);
        return commentRepository.findAll(example);
    }
}