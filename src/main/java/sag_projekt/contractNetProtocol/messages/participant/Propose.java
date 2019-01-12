package sag_projekt.contractNetProtocol.messages.participant;

import sag_projekt.contractNetProtocol.messages.ContractNetProtocolMessage;
import sag_projekt.contractNetProtocol.messages.Performatives;

public class Propose extends ContractNetProtocolMessage {
    public Propose(Double message) {
        this.performative = Performatives.PROPOSE;
        this.message = message;
    }
}
