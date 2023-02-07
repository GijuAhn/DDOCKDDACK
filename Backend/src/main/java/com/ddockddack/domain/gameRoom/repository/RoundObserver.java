package com.ddockddack.domain.gameRoom.repository;

import javax.print.attribute.SetOfIntegerSyntax;
import java.util.Observable;

public class RoundObserver implements Observer {

    private int memberCounter;

    public RoundObserver(Observable subject) {
        subject.addObserver((java.util.Observer) this);
    }

    @Override
    public void update(Observable o, Integer score) {
        if (o instanceof GameMember) {
            GameMember member = (GameMember) o;
            memberCounter++;
        }
        display();
    }

    public void display() {
        System.out.println("Observer counts member... : " + memberCounter);
    }

//    public boolean isAllMemberScored() {
//        GameRoom gameRoom;
//        if (memberCounter == gameRoom.getMembers().size()) {
//            return true;
//        }
//    }

}
