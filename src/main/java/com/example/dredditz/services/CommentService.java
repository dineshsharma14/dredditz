package com.example.dredditz.services;

import com.example.dredditz.dto.CommentDto;
import com.example.dredditz.exceptions.PostNotFoundException;
import com.example.dredditz.exceptions.SpringRedditException;
import com.example.dredditz.mapper.CommentMapper;
import com.example.dredditz.models.Comment;
import com.example.dredditz.models.Post;
import com.example.dredditz.models.User;
import com.example.dredditz.repositories.CommentRepository;
import com.example.dredditz.repositories.PostRepository;
import com.example.dredditz.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public void save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId().toString()));
        User currentUser = authService.getCurrentUser();
        Comment comment = commentMapper.map(commentDto, post, currentUser);
        commentRepository.save(comment);

    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("username not found"));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
