package com.sparta.springlv2project.controller;

import com.sparta.springlv2project.dto.CreateCommentDto;
import com.sparta.springlv2project.dto.boardDto.PostRequestDto;
import com.sparta.springlv2project.dto.boardDto.PostResponseDto;
import com.sparta.springlv2project.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/posting")
    public ResponseEntity<String> posting(@RequestBody PostRequestDto postRequestDto, HttpServletRequest req) {
        boardService.posting(postRequestDto, req);
        return ResponseEntity.status(HttpStatus.OK).body("포스팅 완료 !");
    }

    @GetMapping("/all")
    public List<PostResponseDto> getAllPost() {
        return boardService.getAllPost();
    }

    @GetMapping("/user")
    public List<PostResponseDto> getUserPost(HttpServletRequest req) {
        return boardService.getUserPost(req);
    }

    @GetMapping("/{boardId}")
    public PostResponseDto getPostById(@PathVariable Long boardId) {
        return boardService.getPostById(boardId);
    }

    @PatchMapping("/{boardId}")
    public PostResponseDto patchBoardById(@PathVariable Long boardId, @RequestBody PostRequestDto postRequestDto, HttpServletRequest req) {
        return boardService.patchBoardById(boardId, postRequestDto, req);
    }

    @DeleteMapping("/{boardId}")
    public Long deleteBoardById(@PathVariable Long boardId, HttpServletRequest req) {
        return boardService.deleteBoardById(boardId, req);
    }
}
