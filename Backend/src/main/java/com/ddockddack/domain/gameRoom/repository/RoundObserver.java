package com.ddockddack.domain.gameRoom.repository;

public class RoundObserver implements Observer {

    private int memberCounter;

    private final String name;
    private Subject gameMember;

    public RoundObserver(String name, Subject gameMember){
        this.name = name;
        this.gameMember = gameMember;
    }

    @Override
    public void update() {
        memberCounter++;
        String msg = (String) gameMember.getUpdate(this);
        System.out.println("Observer " + name + " got message >> " + msg);
        System.out.println("Observer counts member... : " + memberCounter);
    }

    /*
    * GameMember gameMember = new GameMember();
    * Observer o = new RoundObserver("GameID", gameMember);
    * gameMember.register(o)
    * */

//    public RoundObserver(Observable subject) {
//        subject.addObserver((java.util.Observer) this);
//    }

//    @Override
//    public void update(Observable o, Integer score) {
//        if (o instanceof GameMember) {
//            GameMember member = (GameMember) o;
//            memberCounter++;
//        }
//        display();
//    }

//    public boolean isAllMemberScored() {
//        GameRoom gameRoom;
//        if (memberCounter == gameRoom.getMembers().size()) {
//            return true;
//        }
//    }

}
