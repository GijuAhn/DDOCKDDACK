package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameMember {
    private String socketId;
    private Member member;
    private String nickname;
    private Integer score = 0;

    public GameMember(String socketId, Member member, String nickname) {
        this.socketId = socketId;
        this.member = member;
        this.nickname = nickname;
    }
}
