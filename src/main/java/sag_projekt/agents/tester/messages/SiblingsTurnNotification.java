package sag_projekt.agents.tester.messages;

public class SiblingsTurnNotification {

    private int turn;

    public SiblingsTurnNotification(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
}
