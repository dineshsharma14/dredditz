package com.example.dredditz.repositories;

import com.example.dredditz.models.Post;
import com.example.dredditz.models.User;
import com.example.dredditz.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    // magic of Spring Data which will write the definition of below mentioned
    // method we just need to follow the heads up provided by Spring Data!
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
