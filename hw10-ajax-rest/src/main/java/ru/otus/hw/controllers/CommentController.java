package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/books/{bookId}/comments")
    public List<Comment> getBookComments(@PathVariable("bookId") long bookId) {
        return commentService.getBookComments(bookId);
    }

    @PostMapping("/books/{bookId}/comments")
    public String insertComment(@PathVariable("bookId") long bookId, @RequestBody String text) {
        try {
            commentService.insertComment(bookId, text);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        return "Ok";
    }

    @DeleteMapping("/books/{bookId}/comments/{id}")
    public void deleteById(@PathVariable("id") long id) {
        try {
            commentService.deleteById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
