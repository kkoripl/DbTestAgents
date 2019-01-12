package sag_projekt.contractNetProtocol.messages.initiator;

import sag_projekt.contractNetProtocol.messages.ContractNetProtocolMessage;
import sag_projekt.contractNetProtocol.messages.Performatives;

public class AcceptProposal extends ContractNetProtocolMessage {
    public AcceptProposal() {
        this.performative = Performatives.ACCEPT_PROPOSAL;
    }
}
