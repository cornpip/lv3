package com.sparta.springlv2project.dto.boardDto;

import com.sparta.springlv2project.entity.Comment;
import com.sparta.springlv2project.entity.Post;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class PostResponseDto {
    private final Long postId;
    private final String subject;
    private final String username;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private List<CommentDto> commentDtoList = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.subject = post.getSubject();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentDtoList = post.getCommentList().stream().map(CommentDto::new).sorted(Comparator.comparing(CommentDto::getCreatedAt).reversed()).collect(Collectors.toList());
    }

    @Getter
    @ToString
    public static class CommentDto {
        private Long id;
        private String username;
        private String contents;
        private LocalDateTime createdAt;

        public CommentDto(Comment comment) {
            this.id = comment.getId();
            this.contents = comment.getContents();
            this.username = comment.getUser().getUsername();
            this.createdAt = comment.getCreatedAt();
        }
    }
}
