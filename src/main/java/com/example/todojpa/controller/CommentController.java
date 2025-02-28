package com.example.todojpa.controller;

import com.example.todojpa.annotation.LoginUser;
import com.example.todojpa.annotation.LoginUserDto;
import com.example.todojpa.dto.request.comment.CommentCreateRequestDto;
import com.example.todojpa.dto.response.comment.CommentResponse;
import com.example.todojpa.service.CommentService;
import jakarta.validation.Valid;
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

    @GetMapping("/{todoId}")
    public ResponseEntity<CommentResponse> getComments(@PathVariable Long todoId) {
        CommentResponse response = commentService.findAllCommentByTodoId(todoId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{todoId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long todoId,
                                                      @RequestBody @Valid CommentCreateRequestDto comment,
                                                      @LoginUser LoginUserDto loginUser) {
        CommentResponse commentResponse = commentService.addComment(todoId, loginUser.getUserId(), comment);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @LoginUser LoginUserDto loginUser) {
        commentService.deleteComment(id, loginUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
