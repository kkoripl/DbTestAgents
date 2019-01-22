package sag_projekt.agents.tester;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import sag_projekt.agents.reporter.messages.AllTestersReadyToTalk;
import sag_projekt.agents.reporter.messages.YouWereFirstReady;
import sag_projekt.agents.tester.actions.DatabaseInvestigation;
import sag_projekt.agents.tester.messages.BestResult;
import sag_projekt.agents.tester.messages.NewSiblingToAdd;
import sag_projekt.agents.tester.messages.ReadyToTalk;
import sag_projekt.agents.tester.messages.WhichNegotiationTurnToStart;
import sag_projekt.contractNetProtocol.messages.initiator.AcceptProposal;
import sag_projekt.contractNetProtocol.messages.initiator.Cfp;
import sag_projekt.contractNetProtocol.messages.initiator.RejectProposal;
import sag_projekt.contractNetProtocol.messages.participant.Propose;
import sag_projekt.contractNetProtocol.messages.participant.Refuse;
import sag_projekt.systemMessages.StartInvestigating;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester extends AbstractActor {

    static public Props props(String dbName) {
        return Props.create(Tester.class, () -> new Tester(dbName));
    }

    private boolean wasFirst = false;
    private String dbName;
    private List<ActorRef> otherTesters;
    private ActorRef reporter;
    private List<Long> investigationResults;
    private Map<ActorRef, Propose> gotProposals = new HashMap<>();
    private int cfpRepliesNo = 0;
    private int actualNegotiationTurn = 0;
    private Cancellable waitForMessagesScheduler;

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Tester(String dbName) {
        this.dbName = dbName;
        this.otherTesters = new ArrayList<>();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NewSiblingToAdd.class, this::addSiblingAsReporterOrTester)
                .match(StartInvestigating.class, si -> {
                    makeDBInvestigation();
                    tellReporterYouAreReady();
                })
                .match(YouWereFirstReady.class, ywfr -> {
                    wasFirst = true;
                    log.info("[" + dbName + "] I was first!");
                })
                .match(AllTestersReadyToTalk.class, atrtt -> checkIfstartProtocol())
                .match(Cfp.class, cfp -> makeReplyForCfp(cfp, getSender()))
                .match(Refuse.class, ref -> {
//                    cfpRepliesNo++;
//                    if(TesterUtils.gotAllProposals(cfpRepliesNo, otherTesters.size())){
//                        sendDecisions();
//                    }
                })
                .match(Propose.class, prop -> {
                    addProposal(prop, getSender());
//                    if(TesterUtils.gotAllProposals(cfpRepliesNo, otherTesters.size())){
//                        sendDecisions();
//                    }
                })
                .match(AcceptProposal.class, acc -> sendResultToReporter())
                .match(WhichNegotiationTurnToStart.class, wntts -> {
                    if(wasntLastNegotiationTurn(wntts.getTurn())){
                        setActualNegotiationTurn(wntts.getTurn());
                        sendCfpForNegotiationTurn(wntts.getTurn());
                    } else {
                        endNegotiations();
                    }
                })
                .build();
    }

    private void addSiblingAsReporterOrTester(NewSiblingToAdd sibling){
        switch(sibling.getClassName()){
            case NewSiblingToAdd.REPORTER:
                log.info("[" + dbName + "] adding reporter!");
                this.setReporter(sibling.getActor());
                break;

            case NewSiblingToAdd.TESTER:
                if(isNotMe(sibling.getActor())) {
                    log.info("[" + dbName + "] adding tester!");
                    otherTesters.add(sibling.getActor());
                }
                break;
        }
    }

    private boolean isNotMe(ActorRef actor){
        ActorRef me = this.getSelf();
        return !actor.equals(me);
    }

    private void makeDBInvestigation(){
        log.info("[" + dbName + "] Start making Investigation!");
        DatabaseInvestigation dbi = new DatabaseInvestigation(dbName);
        this.investigationResults = dbi.getInvestigationResults();
    }

    private void tellReporterYouAreReady(){
        log.info("[" + dbName + "] Tell reporter I'm ready!");
        reporter.tell(new ReadyToTalk(), getSelf());
    }

    private void checkIfstartProtocol(){
        if(wasFirst){
            sendCfpForNegotiationTurn(0);
        }
    }

    private void sendCfpForNegotiationTurn(int turn){
        log.info("[" + dbName + "] Sending CFPs For turn: " + Integer.toString(turn ) + " Value: " + Double.toString(getMyResultForTurn(turn)));
        for(ActorRef tester : otherTesters){
            tester.tell(new Cfp(getMyResultForTurn(turn), this.dbName, turn),getSelf());
        }
        waitForMessagesScheduler = makeWaitForMessagesScheduler(1);
    }

    private Cancellable makeWaitForMessagesScheduler(int waitTimeInSeconds){
        return getContext().getSystem().scheduler()
                .scheduleOnce(Duration.ofSeconds(waitTimeInSeconds),this::sendDecisions, getContext().getSystem().dispatcher());
    }

    private double getMyResultForTurn(int turn){
        return investigationResults.get(turn);
    }

    private void makeReplyForCfp(Cfp cfp, ActorRef sender){
        showGotCfp(cfp);
        if(TesterUtils.myResultWasBetterThanCfp(cfp.getMessage(), getMyResultForTurn(cfp.getNegotiationTurn()))){
            log.info("[" + dbName + "] My result [" + getMyResultForTurn(cfp.getNegotiationTurn()) +"] was better! Sending Propose!");
            sender.tell(new Propose(getMyResultForTurn(cfp.getNegotiationTurn())), getSelf());
        } else {
            log.info("[" + dbName + "] My result [" + getMyResultForTurn(cfp.getNegotiationTurn()) +"]was worse! Sending Refuse!");
            sender.tell(new Refuse(), getSelf());
        }
    }

    private void showGotCfp(Cfp cfp){
        log.info("[" + dbName + "] Got CFP from: " + cfp.getSenderName() + " with value = " + cfp.getMessage());
    }

    private void addProposal(Propose proposal, ActorRef sender){
//        cfpRepliesNo++;
        log.info("[" + dbName + "] Got Proposal: " + proposal.getMessage());
        gotProposals.put(sender, proposal);
    }

    private void sendDecisions(){
//        cfpRepliesNo = 0;
        waitForMessagesScheduler.cancel();
        waitForMessagesScheduler = null;
        if(gotProposals.size() == 0) {
            log.info("[" + dbName + "] No better decisions I'm starting still!");
            sendAllRejects();
            sendResultToReporter();
        } else {
            sendDecisionsToTesters();
        }
        gotProposals.clear();
    }

    private void sendAllRejects(){
        log.info("[" + dbName + "] Sending all rejects!");
        for(ActorRef tester : otherTesters){
            tester.tell(new RejectProposal(), getSelf());
        }
    }

    private void sendResultToReporter(){
        log.info("[" + dbName + "] I'm best! Sending result to reporter! : " + getMyResultForTurn(actualNegotiationTurn));
        reporter.tell(new BestResult(dbName, getMyResultForTurn(actualNegotiationTurn), DatabaseInvestigation.investigationActions.get(actualNegotiationTurn)),getSelf());
    }

    private void sendDecisionsToTesters(){
        log.info("[" + dbName + "] Sending info to other testers about best result!");
        ActorRef bestProposalSender = TesterUtils.getBestProposalSender(gotProposals);
        for(Map.Entry<ActorRef, Propose> proposal : gotProposals.entrySet()){
            if(TesterUtils.isBestSender(bestProposalSender, proposal.getKey())){
                proposal.getKey().tell(new AcceptProposal(), getSelf());
            } else {
                proposal.getKey().tell(new RejectProposal(), getSelf());
            }
        }
    }

    private boolean wasntLastNegotiationTurn(int turn){
        return turn < investigationResults.size();
    }

    public void setActualNegotiationTurn(int actualNegotiationTurn) {
        this.actualNegotiationTurn = actualNegotiationTurn;
    }

    public void endNegotiations(){
        log.info("[" + dbName + "] NEGOTIATIONS'VE ENDED!!!!");
        getContext().getSystem().terminate();
    }

    public void setReporter(ActorRef reporter) {
        this.reporter = reporter;
    }
}
