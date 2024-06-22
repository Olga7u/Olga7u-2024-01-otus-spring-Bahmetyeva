package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Set;

public interface BookRepository extends MongoRepository<Book, String> {

    @Nonnull
    @Query(value = "{}", fields = "{'title': 1, 'author': 1, 'genres': 1}")
    List<Book> findAll();

    @Query(value = "{}", fields = "{'_id': 0, 'author.fullName': 1}")
    Set<String> findDistinctAuthorByFullName();
}
