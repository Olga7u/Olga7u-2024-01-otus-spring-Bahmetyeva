package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Comments")
public class Comment {
    @Id
    private String id;

    private String text;

    private String bookId;

    public Comment(String text, String bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
