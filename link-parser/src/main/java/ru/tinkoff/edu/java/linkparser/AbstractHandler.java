package ru.tinkoff.edu.java.linkparser;

public abstract class AbstractHandler {
    protected AbstractHandler nextHandler;

    public AbstractHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected void handleRequest(Request request) {
        if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }

    abstract boolean defineLink(String link);
}
