package sag_projekt.agents;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import sag_projekt.agents.reporter.Reporter;
import sag_projekt.agents.tester.Tester;
import sag_projekt.agents.tester.messages.NewSiblingToAdd;

import java.util.ArrayList;
import java.util.List;

public class SystemInitiator {
    private ActorSystem system;
    private List<ActorRef> testers;
    private ActorRef reporter;

    public SystemInitiator() {
        initSystem();
        createReporterActor();
        createTesterActors();
        addOtherTestersConnectionToActor(reporter);
    }

    private void initSystem(){
        system = ActorSystem.create("system");
    }

    private void createReporterActor() {
        reporter = system.actorOf(Reporter.props());
    }

    private void createTesterActors(){
        testers = new ArrayList<>();
        testers.add(system.actorOf(Tester.props("Oracle")));
        testers.add(system.actorOf(Tester.props("MySQL")));
        testers.add(system.actorOf(Tester.props("PostgreSQL")));

        for(ActorRef tester : testers){
            addOtherTestersConnectionToActor(tester);
            addReporterConnectionToTester(tester);
        }
    }

    private void addOtherTestersConnectionToActor(ActorRef actor){
        for(ActorRef testerToBeAdded : testers){
            actor.tell(new NewSiblingToAdd(NewSiblingToAdd.TESTER, testerToBeAdded), ActorRef.noSender());
        }
    }

    private void addReporterConnectionToTester(ActorRef tester){
        tester.tell(new NewSiblingToAdd(NewSiblingToAdd.REPORTER, reporter), ActorRef.noSender());
    }

    public ActorSystem getSystem() {
        return system;
    }

    public List<ActorRef> getTesters(){
        return testers;
    }

    public ActorRef getReporter() {
        return reporter;
    }
}
