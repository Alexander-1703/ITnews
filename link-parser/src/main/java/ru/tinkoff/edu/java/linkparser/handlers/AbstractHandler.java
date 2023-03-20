package ru.tinkoff.edu.java.linkparser.handlers;

abstract class AbstractHandler implements Handler {
    protected Handler nextHandler;

    public AbstractHandler(Handler nextHandler) {
        setNextHandler(nextHandler);
    }

    @Override
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
