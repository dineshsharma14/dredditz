package com.example.dredditz.services;

import com.example.dredditz.dto.SubredditDto;
import com.example.dredditz.exceptions.SpringRedditException;
import com.example.dredditz.mapper.SubredditMapper;
import com.example.dredditz.models.Subreddit;
import com.example.dredditz.repositories.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
       Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
       subredditRepository.save(subreddit);
       subredditDto.setId(subreddit.getId());
       return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                //.map(this::mapToDto)
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

//    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//        return Subreddit.builder().name(subredditDto.getName())
//                .description(subredditDto.getDescription())
//                .build();
//    }
//
//    private SubredditDto mapToDto(Subreddit subreddit) {
//        return SubredditDto.builder().name(subreddit.getName())
//                .description(subreddit.getDescription())
//                .id(subreddit.getId())
//                .numberOfPosts(subreddit.getPosts().size())
//                .build();
//    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                    .orElseThrow(() -> new SpringRedditException("No subreddit found with this id."));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
