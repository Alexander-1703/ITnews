package ru.tinkoff.edu.java.linkparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackOverflowHandlerTest {
    @Test
    public void parseTest() {
        AbstractHandler handler = new StackOverflowHandler(null);

        // Проверка, что ссылка на StackOverflow корректно обрабатывается
        Request request = new Request("https://stackoverflow.com/questions/6690745/converting-integer-to-long");
        handler.handleRequest(request);
        assertEquals(6690745L, handler.parse(request.link()));

        request = new Request("https://stackoverflow.com/questions/75736984");
        handler.handleRequest(request);
        assertEquals(75736984L, handler.parse(request.link()));

        request = new Request("stackoverflow.com/questions/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertEquals(75736984L, handler.parse(request.link()));

        request = new Request("https://www.stackoverflow.com/questions/75736984");
        handler.handleRequest(request);
        assertEquals(75736984L, handler.parse(request.link()));

        request = new Request("http://stackoverflow.com/questions/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertEquals(75736984L, handler.parse(request.link()));

        // Проверка, что некорректная ссылка не обрабатывается

        request = new Request("https://stackoverflow.com/questions/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertNull(handler.parse(request.link()));

        request = new Request("https://stackoverflow.com/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertNull(handler.parse(request.link()));

    }

    @Test
    public void defineLinkTest() {
        AbstractHandler handler = new StackOverflowHandler(null);
        // Проверка, что ссылка на другой сайт не обрабатывается

        Request request = new Request("https://github.com/Alexander-1703/ITnews/blob/master/pom.xml");
        handler.handleRequest(request);
        assertFalse(handler.defineLink(request.link()));

        request = new Request("http://queueoverflow.com/questions/75736984/output-of-program-is-inccorect");
        handler.handleRequest(request);
        assertFalse(handler.defineLink(request.link()));
    }
}