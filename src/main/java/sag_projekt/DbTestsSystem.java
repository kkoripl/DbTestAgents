package sag_projekt;

import java.util.List;

import akka.actor.ActorRef;
import sag_projekt.agents.SystemInitiator;
import sag_projekt.systemMessages.StartInvestigating;

public class DbTestsSystem {
  public static void main(String[] args) {
      SystemInitiator initiator = new SystemInitiator();
      List<ActorRef> testers = initiator.getTesters();
          for(ActorRef tester : testers){
              tester.tell(new StartInvestigating(), ActorRef.noSender());
          }
  }
}
