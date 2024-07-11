package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/books/{bookId}/comments")
    public List<Comment> getBookComments(@PathVariable("bookId") long bookId) {
        return commentService.getBookComments(bookId);
    }

    @PostMapping("/api/books/{bookId}/comments")
    public String insertComment(@PathVariable("bookId") long bookId, @RequestBody String text) {
        commentService.insertComment(bookId, text);
        return "Ok";
    }

    @DeleteMapping("/api/books/{bookId}/comments/{id}")
    public void deleteById(@PathVariable("id") long id) {
        commentService.deleteById(id);
    }
}
