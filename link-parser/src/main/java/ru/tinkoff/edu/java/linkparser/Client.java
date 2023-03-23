package ru.tinkoff.edu.java.linkparser;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;

public class Client {
    public static void main(String[] args) {
        Handler handler = HandlerBuilder.build();

        System.out.println(handler.parse(new Request("github.com/Alexander-1703")));
        System.out.println(handler.parse(new Request("damn")));
        System.out.println(handler.parse(new Request("https://www.github.com/Alexander-1703/java-tasks/")));
        System.out.println(handler.parse(new Request("www.github.com/Alexander-1703/java-tasks/")));
        System.out.println(handler.parse(new Request("github.com/Alexander-1703/java-tasks/")));
        System.out.println(handler.parse(new Request("https://stackoverflow.com/questions/75736984/output-of-program-is-inccorect")));
        System.out.println(handler.parse(new Request("stackoverflow.com/questions/75736984")));
        System.out.println(handler.parse(new Request("https://stackoverflow.com/75736984/outpu")));
    }
}
