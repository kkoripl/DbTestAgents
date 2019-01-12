package sag_projekt.agents.tester.messages;

public class BestResult {
    private String bestResultSenderName;
    private double result;

    public BestResult(String bestResultSenderName, double result) {
        this.bestResultSenderName = bestResultSenderName;
        this.result = result;
    }

    public String getBestResultSenderName() {
        return bestResultSenderName;
    }

    public double getResult() {
        return result;
    }
}
