package com.sparta.springlv2project.controller;

import com.sparta.springlv2project.dto.CreateCommentDto;
import com.sparta.springlv2project.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @PostMapping("/board/{boardId}")
    public CreateCommentDto createCommentById(@PathVariable Long boardId, @RequestBody CreateCommentDto requestDto, HttpServletRequest req) {
        return commentService.createCommentById(boardId, requestDto, req);
    }

    @PatchMapping("/{commentId}")
    public CreateCommentDto patchCommentById(@PathVariable Long commentId, @RequestBody CreateCommentDto requestDto, HttpServletRequest req) {
        return commentService.patchCommentById(commentId, requestDto, req);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId, HttpServletRequest req) {
        return commentService.deleteCommentById(commentId, req);
    }
}
