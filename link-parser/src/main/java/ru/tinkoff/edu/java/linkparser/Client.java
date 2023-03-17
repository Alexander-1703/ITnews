package ru.tinkoff.edu.java.linkparser;

public class Client {
    public static void main(String[] args) {
        Handler handler = Handler.handlersChainBuilder();

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
