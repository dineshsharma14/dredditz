package com.example.dredditz.services;

import com.example.dredditz.dto.PostRequest;
import com.example.dredditz.dto.PostResponse;
import com.example.dredditz.exceptions.PostNotFoundException;
import com.example.dredditz.exceptions.SpringRedditException;
import com.example.dredditz.exceptions.SubredditNotFoundException;
import com.example.dredditz.mapper.PostMapper;
import com.example.dredditz.models.Post;
import com.example.dredditz.models.Subreddit;
import com.example.dredditz.models.User;
import com.example.dredditz.repositories.PostRepository;
import com.example.dredditz.repositories.SubredditRepository;
import com.example.dredditz.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException("Subreddit not found!"));
        User currentUser = authService.getCurrentUser();
        postRepository.save(postMapper.map(postRequest, subreddit, currentUser));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
