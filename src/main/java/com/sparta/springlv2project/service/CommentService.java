package com.sparta.springlv2project.service;

import com.sparta.springlv2project.dto.CreateCommentDto;
import com.sparta.springlv2project.dto.boardDto.PostResponseDto;
import com.sparta.springlv2project.entity.Comment;
import com.sparta.springlv2project.entity.Post;
import com.sparta.springlv2project.entity.User;
import com.sparta.springlv2project.entity.UserRoleEnum;
import com.sparta.springlv2project.repository.BoardRepository;
import com.sparta.springlv2project.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    // ADMIN 댓글 수정 가능하게
    @Transactional
    public CreateCommentDto patchCommentById(Long commentId, CreateCommentDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
        if (!user.getRole().getAuthority().equals(UserRoleEnum.ADMIN.getAuthority())) compareUser(user, comment.getUser());
        comment.setContents(requestDto.getContents());
        return requestDto;
    }

    // ADMIN 댓글 삭제 가능하게
    public ResponseEntity<String> deleteCommentById(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
        if (!user.getRole().getAuthority().equals(UserRoleEnum.ADMIN.getAuthority())) compareUser(user, comment.getUser());
        Post post = comment.getPost();
//        System.out.println(new PostResponseDto(post).getCommentDtoList());
        //양방향 객체 지향을 위한 remove
        post.removeCommentList(comment);
        commentRepository.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    //요청 user와 comment user가 동일한지
    public void compareUser(User user1, User user2) {
        if (!user1.equals(user2))
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
    }
}
