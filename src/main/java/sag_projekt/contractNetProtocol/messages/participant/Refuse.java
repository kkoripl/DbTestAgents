package sag_projekt.contractNetProtocol.messages.participant;

import sag_projekt.contractNetProtocol.messages.ContractNetProtocolMessage;
import sag_projekt.contractNetProtocol.messages.Performatives;

public class Refuse extends ContractNetProtocolMessage {
    public Refuse() {
        this.performative = Performatives.REFUSE;
    }
}
