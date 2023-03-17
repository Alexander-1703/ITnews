package ru.tinkoff.edu.java.linkparser;

import ru.tinkoff.edu.java.linkparser.dataobjects.UrlData;

interface Handler {

    void setNextHandler(Handler handler);

    boolean defineLink(String link);

    UrlData parse(Request request);

    static Handler handlersChainBuilder() {
        return new GitHubHandler(new StackOverflowHandler(null));
    }
}
