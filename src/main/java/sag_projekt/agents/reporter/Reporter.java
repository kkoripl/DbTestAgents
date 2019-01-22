package sag_projekt.agents.reporter;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import sag_projekt.agents.reporter.messages.AllTestersReadyToTalk;
import sag_projekt.agents.reporter.messages.YouWereFirstReady;
import sag_projekt.agents.tester.messages.BestResult;
import sag_projekt.agents.tester.messages.NewSiblingToAdd;
import sag_projekt.agents.tester.messages.ReadyToTalk;
import sag_projekt.agents.tester.messages.WhichNegotiationTurnToStart;

import java.util.ArrayList;
import java.util.List;

public class Reporter extends AbstractActor {

    static public Props props() {
        return Props.create(Reporter.class, Reporter::new);
    }

    private List<ActorRef> testers = new ArrayList<>();
    private ActorRef firstReadyTester = null;
    private int testersReadyNo = 0;
    private int maxNegotiationTurn = 0;
    private int negotiationTurn = 0;
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NewSiblingToAdd.class,this::addTesterToContactList)
                .match(ReadyToTalk.class, rtt -> {
                    addReadyTester(getSender());
                    if(allTestersAreReady()){
                        testersReadyNo = 0;
                        startNegotiations();
                    }
                })
                .match(BestResult.class, bs -> {
                    addBestResultToReport(bs);
                    sendWhichTurnToStart(getSender());
                })
                .build();
    }

    private void addReadyTester(ActorRef testerReady){
        log.info("[REPORTER] got ready tester!");
        if(firstReadyTester == null){
            log.info("[REPORTER] SEt first ready!");

            this.firstReadyTester = testerReady;
        }
        else log.info("[REPORTER] wasn't first tester!");
        testersReadyNo++;
        log.info("[REPORTER] Testers ready: " + testersReadyNo);
    }

    private boolean allTestersAreReady(){
        return testersReadyNo == testers.size();
    }

    private void startNegotiations(){
        testersReadyNo = 0;
        log.info("[REPORTER] All testers ready!");
        log.info("[REPORTER] Let's start turn: " + negotiationTurn);
        acknowledgeFirstTesterAboutIt();
        sendAllTestersReadyToTesters();
    }

    private void addTesterToContactList(NewSiblingToAdd tester){
        log.info("[REPORTER] got tester to add!");
        testers.add(tester.getActor());
    }

    private void acknowledgeFirstTesterAboutIt(){
        log.info("[REPORTER] sending info to first tester!");
        firstReadyTester.tell(new YouWereFirstReady(), getSelf());
    }

    private void sendAllTestersReadyToTesters(){
        log.info("[REPORTER] Telling about starting of protocol!");
        for(ActorRef tester : testers){
            tester.tell(new AllTestersReadyToTalk(),getSelf());
        }
    }

    private void addBestResultToReport(BestResult result){
        //@Todo: WYRZUCANIE DO PLIKU JAKOS ROZSADNIE INFORMACJI O NAJLEPSZYM REZULTACIE
        // W Best Result jest aktualnie nazwa zwyciezcy, ktora jest nazwa bazy
        // jesli bedziemy sprawdzac po liczbie rekordow ze slowem to wtedy trzeba w inicjacji aktorow podmienic bazy na slowa
        log.info("[REPORTER] Got Best result to fill in protocol!!!!");
        log.info("------------------> from: " + result.getBestResultSenderName() + " result: " + result.getResult() + ", query: " + result.getQuery());
    }

    private void sendWhichTurnToStart(ActorRef nextNegotiationTurnStarter){
        negotiationTurn++;
        log.info("[REPORTER] Let's start turn: " + negotiationTurn);
        nextNegotiationTurnStarter.tell(new WhichNegotiationTurnToStart(negotiationTurn), getSelf());
    }
}
