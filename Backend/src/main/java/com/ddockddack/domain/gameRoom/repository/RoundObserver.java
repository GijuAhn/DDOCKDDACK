package com.ddockddack.domain.gameRoom.repository;

import java.util.Observable;

public class RoundObserver implements Observer {

    private int memberCounter;

    public RoundObserver(Observable subject) {
        subject.addObserver((java.util.Observer) this);
    }

    @Override
    public void update(Observable o, Integer score) {
        if (score != 0){
            this.memberCounter += 1;
        }
        display();
    }

    public void display() {
        System.out.println("Observer counts member... : " + memberCounter);
    }

}
