package com.example.dredditz.services;

import com.example.dredditz.dto.VoteDto;
import com.example.dredditz.exceptions.PostNotFoundException;
import com.example.dredditz.exceptions.SpringRedditException;
import com.example.dredditz.models.Post;
import com.example.dredditz.models.Vote;
import com.example.dredditz.repositories.PostRepository;
import com.example.dredditz.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.dredditz.models.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() +
                    "'d for this post.");
        }
        if (post.getVoteCount() == null) {
            post.setVoteCount(0);
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
