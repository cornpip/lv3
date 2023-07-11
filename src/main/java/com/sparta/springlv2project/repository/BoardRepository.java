package com.sparta.springlv2project.repository;

import com.sparta.springlv2project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUsername(String username);

    Optional<Post> findByPostIdAndUsername(Long boardId, String username);
}
