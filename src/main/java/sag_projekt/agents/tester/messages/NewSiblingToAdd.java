package sag_projekt.agents.tester.messages;

import akka.actor.ActorRef;

public class NewSiblingToAdd {
    public static final String TESTER = "tester";
    public static final String REPORTER = "reporter";

    private String className;
    private ActorRef actor;

    public NewSiblingToAdd(String className, ActorRef actor) {
        this.className = className;
        this.actor = actor;
    }

    public String getClassName() {
        return className;
    }

    public ActorRef getActor() {
        return actor;
    }
}
