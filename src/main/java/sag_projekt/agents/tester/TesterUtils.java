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
        Map<ActorRef, Propose> sortedProposals = new TreeMap<ActorRef, Propose>(proposals); //@TOdO: sortowanie od najmniejszej do najwiekszej propozycji
                                                                                      // jesli w propozycji bedzie czas realizacji selecta, to zostawic jak jest
                                                                                      // jesli zostaniemy przy wyrazie i liczbie zebranych wierszy to trzeba sortowac odwrotnie

        return getFirstSenderFromSortedProposals(sortedProposals).getKey();
    }

    private static Map.Entry<ActorRef, Propose> getFirstSenderFromSortedProposals(Map<ActorRef, Propose> proposals){
        for(Map.Entry<ActorRef, Propose> proposal : proposals.entrySet()){
            return proposal; // zwracamy tylko pierwszy - najlepszy proposal
        }
        return null; // zaslepka
    }

    public static boolean isBestSender(ActorRef bestSender, ActorRef senderChecked){
        return bestSender.equals(senderChecked);
    }
}
