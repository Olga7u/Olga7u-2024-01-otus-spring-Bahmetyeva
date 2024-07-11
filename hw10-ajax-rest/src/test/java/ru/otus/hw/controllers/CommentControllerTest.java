package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Test
    void getBookComments() throws Exception {
        List<Comment> commentList = List.of(new Comment(1, "bla"), new Comment(2, "bla-bla"));
        given(commentService.getBookComments(1L)).willReturn(commentList);

        mvc.perform(get("/api/books/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentList)));
    }

    @Test
    void insertComment() throws Exception {
        String commentText = "Text";
        mvc.perform(post("/api/books/1/comments")
                        .contentType(APPLICATION_JSON)
                        .content(commentText))
                .andExpect(status().isOk())
                .andExpect(content().string("Ok"));
        verify(commentService, times(1)).insertComment(1L, "Text");
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/api/books/1/comments/1"))
                .andExpect(status().isOk());
        verify(commentService, times(1)).deleteById(1L);
    }
}