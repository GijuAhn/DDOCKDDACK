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
//public class GameMember extends Observable {
public class GameMember implements Subject {
    private String socketId;
    private Member member;
    private String nickname;
    private Integer roundScore = 0;
    private Integer totalScore = 0;
    private List<byte[]> images = new ArrayList<>();

    private List<Observer> observers;
    private String message;

    public GameMember(String socketId, Member member, String nickname) {
        this.socketId = socketId;
        this.member = member;
        this.nickname = nickname;
        this.observers = new ArrayList<>();
        this.message = "";
    }

    @Override
    public void register(Observer obj) {
        if (!observers.contains(obj)) observers.add(obj);
    }

    @Override
    public void unregister(Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
//      ClassName::MethodName <- Method reference
        this.observers.forEach(Observer::update);
    }

    @Override
    public Object getUpdate(Observer obj) {
        return this.message;
    }

    public void postMessage(String msg){
        System.out.println("Message from Member: "+ msg);
        this.message = msg;
        notifyObservers();
    }

    public void changeScore(int score) {
        this.roundScore = score;
        this.totalScore += score;
        postMessage("GameMember roundScore & totalScore is updated");
    }
}
