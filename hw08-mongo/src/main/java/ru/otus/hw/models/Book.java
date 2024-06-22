package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Books")
public class Book {
    @Id
    private String id;

    @Field
    private String title;

    private Author author;

    private Set<Genre> genres;

    @DBRef
    private List<Comment> comments;

    public Book(String title) {
        this.title = title;
    }

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Book(String id, String title, Author author, Set<Genre> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}