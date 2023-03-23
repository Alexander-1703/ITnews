package ru.tinkoff.edu.java.linkparser;


import org.junit.jupiter.api.Test;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.handlers.GitHubHandler;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class GitHubHandlerTest {
    @Test
    public void parseTest() {
        Handler handler = new GitHubHandler(null);

        // Проверка, что ссылка на GitHub корректно обрабатывается
        Request request = new Request("https://github.com/Alexander-1703/ITnews/blob/master/pom.xml");
        assertEquals(new GitHubData("Alexander-1703", "ITnews"),
                handler.parse(request));

        request = new Request("https://github.com/Alexander-1703/ITnews");
        assertEquals(new GitHubData("Alexander-1703", "ITnews"),
                handler.parse(request));

        // Проверка, что некорректная ссылка не обрабатывается
        request = new Request("https://github.com/Alexander-1703/");
        assertNull(handler.parse(request));

        request = new Request("https://github.com");
        assertNull(handler.parse(request));

    }

    @Test
    public void defineLinkTest() {
        Handler handler = new GitHubHandler(null);

        // Проверка, что ссылка на другой сайт не обрабатывается
        Request request = new Request("https://stackoverflow.com/questions/75736984/output-of-program-is-inccorect");
        assertFalse(handler.defineLink(request.link()));

        request = new Request("https://gitlab.com/questions/75736984/output-of-program-is-inccorect");
        assertFalse(handler.defineLink(request.link()));

        request = new Request("https://github.ru/questions/75736984/output-of-program-is-inccorect");
        assertFalse(handler.defineLink(request.link()));
    }
}