package ru.tinkoff.edu.java.linkparser;

import org.junit.jupiter.api.Test;

import ru.tinkoff.edu.java.linkparser.dataobjects.StackOverflowData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class StackOverflowHandlerTest {
    @Test
    public void parseTest() {
        Handler handler = new StackOverflowHandler(null);

        // Проверка, что ссылка на StackOverflow корректно обрабатывается
        Request request = new Request("https://stackoverflow.com/questions/6690745/converting-integer-to-long");
        assertEquals(new StackOverflowData(6690745L), handler.parse(request));

        request = new Request("https://stackoverflow.com/questions/75736984");
        assertEquals(new StackOverflowData(75736984L), handler.parse(request));

        request = new Request("stackoverflow.com/questions/75736984/output-of-program-is-inccorect");
        assertEquals(new StackOverflowData(75736984L), handler.parse(request));

        request = new Request("https://www.stackoverflow.com/questions/75736984");
        assertEquals(new StackOverflowData(75736984L), handler.parse(request));

        request = new Request("http://stackoverflow.com/questions/75736984/output-of-program-is-inccorect");
        assertEquals(new StackOverflowData(75736984L), handler.parse(request));

        // Проверка, что некорректная ссылка не обрабатывается

        request = new Request("https://stackoverflow.com/questions/output-of-program-is-inccorect");
        assertNull(handler.parse(request));

        request = new Request("https://stackoverflow.com/75736984/output-of-program-is-inccorect");
        assertNull(handler.parse(request));

    }

    @Test
    public void defineLinkTest() {
        Handler handler = new StackOverflowHandler(null);
        // Проверка, что ссылка на другой сайт не обрабатывается

        Request request = new Request("https://github.com/Alexander-1703/ITnews/blob/master/pom.xml");
        assertFalse(handler.defineLink(request.link()));

        request = new Request("http://queueoverflow.com/questions/75736984/output-of-program-is-inccorect");
        assertFalse(handler.defineLink(request.link()));
    }
}