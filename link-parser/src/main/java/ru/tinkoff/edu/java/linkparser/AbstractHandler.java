package ru.tinkoff.edu.java.linkparser;

public abstract class AbstractHandler {
    protected AbstractHandler nextHandler;

    public AbstractHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected Object handleRequest(Request request) {
        if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
        return null;
    }

    abstract protected boolean defineLink(String link);
}
