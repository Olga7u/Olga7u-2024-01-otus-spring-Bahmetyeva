package ru.otus.hw.listeners;

import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;


@Component
@AllArgsConstructor
public class BookDeleteListener extends AbstractMongoEventListener<Book> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        Document document = event.getSource();
        if (!document.containsKey("_id")) {
            return;
        }

        String bookId = document.get("_id", Object.class).toString();

        Query query = new Query(Criteria.where("bookId").is(bookId));
        mongoTemplate.remove(query, Comment.class);
    }
}
