package com.example.dredditz.repositories;

import com.example.dredditz.models.Comment;
import com.example.dredditz.models.Post;
import com.example.dredditz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}
