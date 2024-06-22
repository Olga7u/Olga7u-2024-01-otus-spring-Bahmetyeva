package ru.otus.hw.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

import java.util.Set;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", runAlways = true, author = "")
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertBook1", author = "")
    public void insertBook(BookRepository bookRepository) {
        Book book = new Book("10", "Book1");
        book.setAuthor(new Author("Author1"));
        book.setGenres(Set.of(new Genre[]{new Genre("Genre1"), new Genre("Genre2")}));
        bookRepository.save(book);
    }
}
