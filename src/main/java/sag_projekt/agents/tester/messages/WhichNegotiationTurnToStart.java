package sag_projekt.agents.tester.messages;

public class WhichNegotiationTurnToStart {
    private int turn;

    public WhichNegotiationTurnToStart(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
}
