package com.ddockddack.domain.gameRoom.response;

import com.ddockddack.domain.gameRoom.repository.GameMember;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class GameMemberRes {
    private String socketId;
    private byte[] image;
    private int roundScore;

    @Builder
    public GameMemberRes(String socketId, byte[] image, int roundScore) {
        this.socketId = socketId;
        this.image = image;
        this.roundScore = roundScore;
    }

    public static GameMemberRes from(GameMember gameMember, int round){
        return GameMemberRes.builder()
                .socketId(gameMember.getSocketId())
                .image(gameMember.getImages().get(round))
                .roundScore(gameMember.getRoundScore())
                .build();
    }

    public static GameMemberRes from(GameMember gameMember){
        return GameMemberRes.builder()
                .socketId(gameMember.getSocketId())
                .roundScore(gameMember.getTotalScore())
                .build();
    }
}
