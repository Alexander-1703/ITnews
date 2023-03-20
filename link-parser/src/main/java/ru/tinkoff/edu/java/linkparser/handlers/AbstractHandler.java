package ru.tinkoff.edu.java.linkparser.handlers;

abstract class AbstractHandler implements Handler {
    protected Handler nextHandler;

    public AbstractHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
