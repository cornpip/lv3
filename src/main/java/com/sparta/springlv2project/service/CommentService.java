package com.sparta.springlv2project.service;

import com.sparta.springlv2project.dto.CreateCommentDto;
import com.sparta.springlv2project.entity.Comment;
import com.sparta.springlv2project.entity.Post;
import com.sparta.springlv2project.entity.User;
import com.sparta.springlv2project.repository.BoardRepository;
import com.sparta.springlv2project.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentService {
    private BoardRepository boardRepository;
    private CommentRepository commentRepository;

    public CreateCommentDto createCommentById(Long boardId, CreateCommentDto requestDto, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        Post post = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
        Comment comment = new Comment();
        comment.setContents(requestDto.getContents());
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
        return requestDto;
    }

    @Transactional
    public CreateCommentDto patchCommentById(Long commentId, CreateCommentDto requestDto, HttpServletRequest req) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comparePostUserAndUser(req, comment.getUser());
        comment.setContents(requestDto.getContents());
        return requestDto;
    }

    public ResponseEntity<String> deleteCommentById(Long commentId, HttpServletRequest req) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comparePostUserAndUser(req, comment.getUser());
        commentRepository.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    public void comparePostUserAndUser(HttpServletRequest req, User user) {
        User reqUser = (User) req.getAttribute("user");
        if (!reqUser.equals(user))
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다");
    }
}