package com.ddockddack.domain.gameRoom.response;

import com.ddockddack.domain.game.response.GameDetailRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class GameRoomRes {

    private String token;
    private String pinNumber;

    private GameDetailRes gameDetailRes;

    @Builder
    public GameRoomRes(String token, String pinNumber, GameDetailRes gameDetailRes) {
        this.token = token;
        this.pinNumber = pinNumber;
        this.gameDetailRes = gameDetailRes;
    }
}
