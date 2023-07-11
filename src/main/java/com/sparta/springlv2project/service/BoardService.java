package com.sparta.springlv2project.service;

import com.sparta.springlv2project.dto.CreateCommentDto;
import com.sparta.springlv2project.dto.boardDto.PostResponseDto;
import com.sparta.springlv2project.dto.boardDto.PostRequestDto;
import com.sparta.springlv2project.entity.Comment;
import com.sparta.springlv2project.entity.Post;
import com.sparta.springlv2project.entity.User;
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


    @Transactional
    public PostResponseDto patchBoardById(Long boardId, PostRequestDto requestDto, HttpServletRequest req) {
        Claims userInfo = getUserInfoFromRequest(req);
        Post post = comparePostUserAndUser(boardId, userInfo.getSubject());
        post.setSubject(requestDto.getSubject());
        post.setContents(requestDto.getContents());
        return new PostResponseDto(post);
    }

    public Long deleteBoardById(Long boardId, HttpServletRequest req) {
        Claims userInfo = getUserInfoFromRequest(req);
        Post post = comparePostUserAndUser(boardId, userInfo.getSubject());
        boardRepository.deleteById(boardId);
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
        throw new IllegalArgumentException("권한이 없는 유저입니다.");
    }

}
