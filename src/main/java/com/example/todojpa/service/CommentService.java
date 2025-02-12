package com.example.todojpa.service;

import com.example.todojpa.dto.request.comment.CommentCreateRequestDto;
import com.example.todojpa.dto.request.comment.CommentUpdateRequestDto;
import com.example.todojpa.dto.response.comment.CommentDetail;
import com.example.todojpa.dto.response.comment.CommentResponse;
import com.example.todojpa.entity.Comment;
import com.example.todojpa.entity.Todo;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.comment.CommentRepository;
import com.example.todojpa.repository.todo.TodoRepository;
import com.example.todojpa.repository.user.UserRepository;
import com.example.todojpa.security.MySecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TodoRepository todoRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    public CommentResponse findAllCommentByTodoId(Long todoId) {
        List<Comment> comments = commentRepository.findAllByTodoIdOrderByLevelAscCreatedAtDesc(todoId);
        return CommentResponse.from(CommentDetail.convertTree(comments));
    }

    @Transactional
    public CommentResponse addComment(CommentCreateRequestDto requestDto, Long todoId) {
        User from = userRepository.findById(getUserIdFromContext()).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Todo to = todoRepository.findById(todoId).orElseThrow(()->new ApplicationException(ErrorCode.TODO_NOT_FOUND));
        Comment comment = Comment.builder()
                .user(from)
                .todo(to)
                .comment(requestDto.getComment())
                .level(0)
                .build();
        if (requestDto.getParent() != null){//일반 댓글이라면
            Comment parent = commentRepository.findById(requestDto.getParent()).orElseThrow(()->new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));
            if(!parent.getTodo().getId().equals(todoId)){
                //동일한 게시물이 아니라면
                throw new ApplicationException(ErrorCode.BAD_REQUEST);
            }
            comment.setParent(parent);
            comment.setLevel(parent.getLevel() + 1);
        }

        Comment save = commentRepository.save(comment);

        return CommentResponse.from(save);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUser().getId().equals(getUserIdFromContext())) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUser().getId().equals(getUserIdFromContext())) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }

        comment.updateComment(requestDto.getComment());
    }

    public Long getUserIdFromContext() {
        if(MySecurityContextHolder.getAuthenticated() != null && MySecurityContextHolder.getAuthenticated().getIsValid()) {
            return MySecurityContextHolder.getAuthenticated().getUserId();
        } else {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
    }

}
