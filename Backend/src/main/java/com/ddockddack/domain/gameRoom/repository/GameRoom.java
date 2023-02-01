package com.ddockddack.domain.gameRoom.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {
    private String pinNumber;

    private Long gameId;

    private boolean isStarted;



    private Map<String, GameMember> members = new ConcurrentHashMap<>();

    @Builder
    public GameRoom(String pinNumber, Long gameId) {
        this.pinNumber = pinNumber;
        this.gameId = gameId;
    }

    public void start() {
        this.isStarted = true;
    }
}
