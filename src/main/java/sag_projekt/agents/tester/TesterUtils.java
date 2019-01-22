package sag_projekt.agents.tester;

import akka.actor.ActorRef;
import sag_projekt.contractNetProtocol.messages.participant.Propose;

import java.util.Map;
import java.util.TreeMap;

public class TesterUtils {
    public static boolean myResultWasBetterThanCfp(double cfpResult, double myResult){
        return myResult < cfpResult;
    }

    public static boolean gotAllProposals(int repliesCounter, int testersCounter){
        return repliesCounter == testersCounter;
    }

    public static ActorRef getBestProposalSender(Map<ActorRef, Propose> proposals){
        ActorRef winner = null;
        // get first as default
        for(Map.Entry<ActorRef, Propose> proposal : proposals.entrySet()){
            winner = proposal.getKey();
            break;
        }
        double bestTime = Double.MAX_VALUE;
        for (Map.Entry<ActorRef, Propose> proposal : proposals.entrySet()) {
            if (proposal.getValue().getMessage() < bestTime) {
                bestTime = proposal.getValue().getMessage();
                winner = proposal.getKey();
            }
        }

        return winner;
    }

    public static boolean isBestSender(ActorRef bestSender, ActorRef senderChecked){
        return bestSender.equals(senderChecked);
    }
}
