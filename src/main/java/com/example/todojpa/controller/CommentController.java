package com.example.todojpa.controller;

import com.example.todojpa.dto.request.CommentCreateRequestDto;
import com.example.todojpa.dto.response.CommentResponse;
import com.example.todojpa.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{todoId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long todoId,
                                                      @RequestBody CommentCreateRequestDto comment) {
        CommentResponse commentResponse = commentService.addComment(comment, todoId);
        return ResponseEntity.ok(commentResponse);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
