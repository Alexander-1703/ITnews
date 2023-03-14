package ru.tinkoff.edu.java.linkparser;


import java.util.AbstractMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitHubHandlerTest {
    @Test
    public void getUserAndRepositoryTest() {
        GitHubHandler handler = new GitHubHandler(null);

        // Проверка, что ссылка на GitHub корректно обрабатывается
        Request request = new Request("https://github.com/Alexander-1703/ITnews/blob/master/pom.xml");
        handler.handleRequest(request);
        assertEquals(new AbstractMap.SimpleEntry<String, String> ("Alexander-1703", "ITnews"),
                handler.getUserAndRepository(request.link()));

        request = new Request("https://github.com/Alexander-1703/ITnews");
        handler.handleRequest(request);
        assertEquals(new AbstractMap.SimpleEntry<String, String> ("Alexander-1703", "ITnews"),
                handler.getUserAndRepository(request.link()));

        // Проверка, что некорректная ссылка не обрабатывается
        request = new Request("https://github.com/Alexander-1703/");
        handler.handleRequest(request);
        assertNull(handler.getUserAndRepository(request.link()));

        request = new Request("https://github.com");
        handler.handleRequest(request);
        assertNull(handler.getUserAndRepository(request.link()));

    }

    @Test
    public void defineLinkTest() {
        GitHubHandler handler = new GitHubHandler(null);

        // Проверка, что ссылка на другой сайт не обрабатывается
        Request request = new Request("https://stackoverflow.com/questions/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertFalse(handler.defineLink(request.link()));

        request = new Request("https://gitlab.com/questions/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertFalse(handler.defineLink(request.link()));

        request = new Request("https://github.ru/questions/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertFalse(handler.defineLink(request.link()));

    }
}