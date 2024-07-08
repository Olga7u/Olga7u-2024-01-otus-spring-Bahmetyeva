package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController extends RootController {

    private final CommentService commentService;

    private final String apiUrl = "/api/books/{bookId}/comments";

    @GetMapping(apiUrl)
    public List<Comment> getBookComments(@PathVariable("bookId") long bookId) {
        return commentService.getBookComments(bookId);
    }

    @PostMapping(apiUrl)
    public String insertComment(@PathVariable("bookId") long bookId, @RequestBody String text) {
        commentService.insertComment(bookId, text);
        return "Ok";
    }

    @DeleteMapping(apiUrl + "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        commentService.deleteById(id);
    }
}
