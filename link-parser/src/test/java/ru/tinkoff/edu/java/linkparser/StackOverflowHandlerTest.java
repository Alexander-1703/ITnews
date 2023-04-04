package ru.tinkoff.edu.java.linkparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;
import ru.tinkoff.edu.java.linkparser.handlers.StackOverflowHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class StackOverflowHandlerTest {
    private Handler handler;
    private Request request;

    @BeforeEach
    public void setup() {
        handler = new StackOverflowHandler(null);
    }

    @ParameterizedTest(name = "{index}: {0} is processed to {1}")
    @CsvSource(value = {
            "https://stackoverflow.com/questions/6690745/converting-integer-to-long, 6690745",
            "https://stackoverflow.com/questions/75736984, 75736984",
            "stackoverflow.com/questions/75736984/output-of-program-is-inccorect, 75736984",
            "https://www.stackoverflow.com/questions/75736984, 75736984",
            "http://stackoverflow.com/questions/75736984/output-of-program-is-inccorect, 75736984"
    })
    @DisplayName("a test that checks that valid stackoverflow links are being processed")
    public void correctStackoverflowLinkTest(String link, long id) {
        request = new Request(link);
        assertEquals(new StackOverflowData(id), handler.parse(request));
    }

    @ParameterizedTest(name = "{index}: {0} not processed")
    @ValueSource(strings = {
            "https://stackoverflow.com/questions/output-of-program-is-inccorect",
            "https://stackoverflow.com/75736984/output-of-program-is-inccorect"
    })
    @DisplayName("a test that checks that invalid links to stackoverflow are not processed")
    public void stackoverflowBadLinkTest() {

    }



    @ParameterizedTest(name = "{index}: {0} not processed")
    @ValueSource(strings = {
            "https://github.com/Alexander-1703/ITnews/blob/master/pom.xml",
            "http://queueoverflow.com/questions/75736984/output-of-program-is-inccorect"
    })
    @DisplayName("a test that checks that a link not to a stackoverflow is not processed")
    public void defineLinkTest(String link) {
        request = new Request(link);
        assertFalse(handler.defineLink(request.link()));
    }
}