package ru.tinkoff.edu.java.linkparser;

public class Client {
    public static void main(String[] args) {
        AbstractHandler handler = new GitHubHandler(new StackOverflowHandler(null));

        System.out.println(handler.handleRequest(new Request("damn")));
        System.out.println(handler.handleRequest(new Request("github.com/Alexander-1703/java-tasks/")));
        System.out.println(handler.handleRequest(new Request("https://stackoverflow.com/questions/75736984/output-of-program-is-inccorect")));
        System.out.println(handler.handleRequest(new Request("github.com/Alexander-1703/")));
        System.out.println(handler.handleRequest(new Request("https://stackoverflow.com/75736984/outpu")));
    }
}
