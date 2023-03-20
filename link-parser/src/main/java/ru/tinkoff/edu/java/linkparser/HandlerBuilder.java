package ru.tinkoff.edu.java.linkparser;

import ru.tinkoff.edu.java.linkparser.handlers.GitHubHandler;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;
import ru.tinkoff.edu.java.linkparser.handlers.StackOverflowHandler;

public class HandlerBuilder {
    public static Handler build() {
        return new GitHubHandler(new StackOverflowHandler(null));
    }
}
