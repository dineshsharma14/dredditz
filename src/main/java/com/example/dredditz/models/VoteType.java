package com.example.dredditz.models;



import com.example.dredditz.exceptions.SpringRedditException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1), ;

    private int direction;

    private VoteType(int direction) {

    }

    public static VoteType lookup(Integer direction) {
        return  Arrays.stream(VoteType.values())
                    .filter(value -> value.getDirection().equals(direction))
                    .findAny()
                    .orElseThrow(() -> new SpringRedditException("Vote not found!"));
    }

    public Integer getDirection() {
        return direction;
    }

}
