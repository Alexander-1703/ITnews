package ru.tinkoff.edu.java.linkparser.handlers;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;

public interface Handler {

    void setNextHandler(Handler handler);

    boolean defineLink(String link);

    UrlData parse(Request request);

    static Handler handlersChainBuilder() {
        return new GitHubHandler(new StackOverflowHandler(null));
    }
}
