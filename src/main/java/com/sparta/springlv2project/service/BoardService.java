package com.sparta.springlv2project.service;

import com.sparta.springlv2project.dto.boardDto.PostRequestDto;
import com.sparta.springlv2project.dto.boardDto.PostResponseDto;
import com.sparta.springlv2project.entity.Post;
import com.sparta.springlv2project.entity.User;
import com.sparta.springlv2project.entity.UserRoleEnum;
import com.sparta.springlv2project.jwt.JwtUtil;
import com.sparta.springlv2project.repository.BoardRepository;
import com.sparta.springlv2project.repository.CommentRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BoardService {


    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository, JwtUtil jwtUtil) {
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
    }

    public void posting(PostRequestDto postRequestDto, HttpServletRequest req) {
        Claims userInfo = getUserInfoFromRequest(req);
        Post post = new Post(postRequestDto, userInfo);
        boardRepository.save(post);
    }


    public List<PostResponseDto> getAllPost() {
        return boardRepository.findAll().stream().map(PostResponseDto::new).toList();
    }

    public List<PostResponseDto> getUserPost(HttpServletRequest req) {
        Claims userInfo = getUserInfoFromRequest(req);
        return boardRepository.findAllByUsername(userInfo.getSubject()).stream().map(PostResponseDto::new).toList();
    }

    // ADMIN 게시글 수정
    @Transactional
    public PostResponseDto patchBoardById(Long boardId, PostRequestDto requestDto, User user) {

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            Post post = boardRepository.findById(boardId).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다"));
            post.update(requestDto);
            return new PostResponseDto(post);

        } else {
            //유저의 권한이 ADMIN이 아니면 아이디가 같은 유저만 수정 가능
            Optional<Post> post = boardRepository.findByPostIdAndUsername(boardId, user.getUsername());

            if (post.isPresent()) {
                post.get().update(requestDto);
                return new PostResponseDto(post.get());

            } else {
                throw new IllegalArgumentException("접 근 불 가");
            }
        }
    }

    // ADMIN 게시글 삭제
    public Long deleteBoardById(Long boardId, User user) {
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            Post post = boardRepository.findById(boardId).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다"));
            boardRepository.delete(post);
        } else {
            // 유저의 권한이 ADMIN이 아니면 아이디가 같은 유저만 삭제 가능
            Optional<Post> post = boardRepository.findByPostIdAndUsername(boardId, user.getUsername());
            if (post.isPresent()) {
                boardRepository.delete(post.get());
            } else {
                throw new IllegalArgumentException("접 근 불 가");
            }
        }
        return boardId;
    }

    public PostResponseDto getPostById(Long boardId) {
        Post post = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId 입니다."));
        return new PostResponseDto(post);
    }

    private Claims getUserInfoFromRequest(HttpServletRequest req) {
        String Token = jwtUtil.getTokenFromRequest(req);
        return jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(Token));
    }

    private Post comparePostUserAndUser(Long boardId, String username) {
        Post post = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId 입니다."));
        if (Objects.equals(post.getUsername(), username)) return post;
        throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
    }

}
