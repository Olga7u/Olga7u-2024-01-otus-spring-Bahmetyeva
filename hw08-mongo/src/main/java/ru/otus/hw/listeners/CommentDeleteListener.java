package ru.otus.hw.listeners;

import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

@Component
@AllArgsConstructor
public class CommentDeleteListener extends AbstractMongoEventListener<Comment> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Comment> event) {
        Document document = event.getSource();
        if (!document.containsKey("_id")) {
            return;
        }

        String commentId = document.get("_id", Object.class).toString();

        Comment comment = mongoTemplate.findById(commentId, Comment.class);
        if (comment == null) {
            return;
        }

        Book book = mongoTemplate.findById(comment.getBookId(), Book.class);
        if (book == null) {
            return;
        }

        List<Comment> newComments = book.getComments();
        newComments.remove(comment);
        book.setComments(newComments);

        mongoTemplate.save(book);
    }
}
