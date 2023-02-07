package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

// Subject class
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameMember extends Observable {
    private String socketId;
    private Member member;
    private String nickname;
    private Integer roundScore = 0;
    private Integer totalScore = 0;
    private List<byte[]> images = new ArrayList<>();

    public GameMember(String socketId, Member member, String nickname) {
        this.socketId = socketId;
        this.member = member;
        this.nickname = nickname;
    }

    public void changeScore(int roundScore){
        setChanged();
        notifyObservers(roundScore);
    }

    public void changeScore(){
        setChanged();
        notifyObservers();
    }

}
