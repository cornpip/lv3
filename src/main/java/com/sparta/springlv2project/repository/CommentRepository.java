package com.sparta.springlv2project.repository;

import com.sparta.springlv2project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
