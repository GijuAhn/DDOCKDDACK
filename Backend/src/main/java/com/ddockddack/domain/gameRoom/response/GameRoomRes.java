package com.ddockddack.domain.gameRoom.response;

import com.ddockddack.domain.game.response.GameImageRes;
import com.ddockddack.domain.gameRoom.repository.GameRoom;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameRoomRes {

    private String token;
    private String pinNumber;
    private Long gameId;
    private String gameTitle;
    private String gameDescription;
    private List<GameImageRes> gameImages;

    @Builder
    public GameRoomRes(String token, String pinNumber, Long gameId, String gameTitle,
        String gameDescription, List<GameImageRes> gameImages) {
        this.token = token;
        this.pinNumber = pinNumber;
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.gameDescription = gameDescription;
        this.gameImages = gameImages;
    }

    public static GameRoomRes of(GameRoom gameRoom) {
        return GameRoomRes.builder()
            .pinNumber(gameRoom.getPinNumber())
            .gameId(gameRoom.getGameId())
            .gameTitle(gameRoom.getGameTitle())
            .gameDescription(gameRoom.getGameDescription())
            .gameImages(gameRoom.getGameImages().stream()
                .map(i -> GameImageRes.of(i))
                .collect(Collectors.toList()))
            .build();
    }
}
