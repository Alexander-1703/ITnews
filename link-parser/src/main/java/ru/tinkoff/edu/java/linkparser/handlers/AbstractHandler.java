package ru.tinkoff.edu.java.linkparser.handlers;

abstract class AbstractHandler implements Handler {
    protected Handler nextHandler;

    AbstractHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
