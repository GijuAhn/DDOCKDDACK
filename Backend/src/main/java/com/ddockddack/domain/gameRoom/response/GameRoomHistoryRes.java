package com.ddockddack.domain.gameRoom.response;

import com.ddockddack.domain.gameRoom.entity.GameRoomHistory;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@ToString
@NoArgsConstructor
public class GameRoomHistoryRes {

    private String gameTitle;

    private int ranking;

    private LocalDate playedTime;

    @Builder
    public GameRoomHistoryRes(String gameTitle, int ranking, LocalDate playedTime) {
        this.gameTitle = gameTitle;
        this.ranking = ranking;
        this.playedTime = playedTime;
    }

    public static GameRoomHistoryRes of(GameRoomHistory gameRoomHistory) {
        return GameRoomHistoryRes
                .builder()
                .gameTitle(gameRoomHistory.getGameTitle())
                .ranking(gameRoomHistory.getRanking())
                .playedTime(gameRoomHistory.getCreateAt())
                .build();
    }

}
