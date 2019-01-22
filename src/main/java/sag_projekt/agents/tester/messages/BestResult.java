package sag_projekt.agents.tester.messages;

public class BestResult {
    private String bestResultSenderName;
    private String query;
    private double result;

    public BestResult(String bestResultSenderName, double result, String query) {
        this.bestResultSenderName = bestResultSenderName;
        this.result = result;
        this.query = query;
    }

    public String getBestResultSenderName() {
        return bestResultSenderName;
    }

    public double getResult() {
        return result;
    }

    public String getQuery() {
        return query;
    }
}
