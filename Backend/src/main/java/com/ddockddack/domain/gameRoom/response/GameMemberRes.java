package com.ddockddack.domain.gameRoom.response;

import com.ddockddack.domain.gameRoom.repository.GameMember;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class GameMemberRes {
    private String socketId;
    private String nickname;
    private byte[] image;
    private int roundScore;
    private int scaledRoundScore;

    @Builder
    public GameMemberRes(String socketId, String nickname, byte[] image, int roundScore, int scaledRoundScore) {
        this.socketId = socketId;
        this.image = image;
        this.roundScore = roundScore;
        this.scaledRoundScore = scaledRoundScore;
        this.nickname = nickname;
    }

    public static GameMemberRes from(GameMember gameMember, int round){
        return GameMemberRes.builder()
                .socketId(gameMember.getSocketId())
                .nickname(gameMember.getNickname())
                .image(gameMember.getImages().get(round))
                .roundScore(gameMember.getRoundScore())
                .scaledRoundScore(gameMember.getScaledRoundScore())
                .build();
    }

    public static GameMemberRes from(GameMember gameMember){
        return GameMemberRes.builder()
                .socketId(gameMember.getSocketId())
                .nickname(gameMember.getNickname())
                .roundScore(gameMember.getTotalScore())
                .build();
    }
}
