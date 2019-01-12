package sag_projekt.contractNetProtocol.messages.initiator;

import sag_projekt.contractNetProtocol.messages.ContractNetProtocolMessage;
import sag_projekt.contractNetProtocol.messages.Performatives;

public class RejectProposal extends ContractNetProtocolMessage {
    public RejectProposal() {
        this.performative = Performatives.REJECT_PROPOSAL;
    }
}
