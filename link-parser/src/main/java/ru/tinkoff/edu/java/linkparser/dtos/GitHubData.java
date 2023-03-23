package ru.tinkoff.edu.java.linkparser.dtos;

public record GitHubData(String username, String repos) implements UrlData {
}
