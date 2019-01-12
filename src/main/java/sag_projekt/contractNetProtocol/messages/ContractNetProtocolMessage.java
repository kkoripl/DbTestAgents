package sag_projekt.contractNetProtocol.messages;

public abstract class ContractNetProtocolMessage {
    protected String performative;
    protected Double message;
    protected String senderName;

    public String getPerformative() {
        return performative;
    }

    public Double getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }
}
