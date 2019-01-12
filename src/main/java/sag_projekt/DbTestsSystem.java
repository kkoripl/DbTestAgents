package sag_projekt;

import java.io.IOException;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import sag_projekt.agents.SystemInitiator;
import sag_projekt.systemMessages.StartInvestigating;

public class DbTestsSystem {
  public static void main(String[] args) {
      SystemInitiator initiator = new SystemInitiator();
      final ActorSystem system = initiator.getSystem();
      List<ActorRef> testers = initiator.getTesters();

      try {
          for(ActorRef tester : testers){
              tester.tell(new StartInvestigating(), ActorRef.noSender());
          }
          System.out.println(">>> Press ENTER to exit <<<");
           System.in.read();
      } catch (IOException ioe) {
      } finally {
          system.terminate();
      }
  }
}
