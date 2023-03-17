package ru.tinkoff.edu.java.linkparser.dataobjects;

public record GitHubData(String username, String repos) implements UrlData {
}
