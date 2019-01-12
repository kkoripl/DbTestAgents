package sag_projekt.contractNetProtocol.messages.initiator;

import sag_projekt.contractNetProtocol.messages.ContractNetProtocolMessage;
import sag_projekt.contractNetProtocol.messages.Performatives;

public class Cfp extends ContractNetProtocolMessage {
    private int negotiationTurn;

    public Cfp(Double message, String senderName, int negotiationTurn) {
        this.performative = Performatives.CFP;
        this.message = message;
        this.senderName = senderName;
        this.negotiationTurn = negotiationTurn;
    }

    public int getNegotiationTurn() {
        return negotiationTurn;
    }
}
