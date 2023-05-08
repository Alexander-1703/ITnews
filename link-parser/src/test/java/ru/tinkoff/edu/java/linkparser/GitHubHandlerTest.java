package ru.tinkoff.edu.java.linkparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.handlers.GitHubHandler;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class GitHubHandlerTest {
    private Handler handler;
    private Request request;

    @BeforeEach
    public void setup() {
        handler = new GitHubHandler(null);
    }

    @ParameterizedTest(name = "{index}: {0} is processed to {1}:{2}")
    @CsvSource(value = {
        "https://github.com/Alexander-1703/ITnews/blob/master/pom.xml, Alexander-1703, ITnews",
        "https://github.com/Alexander-1703/ITnews, Alexander-1703, ITnews"
    })
    @DisplayName("a test that checks that valid github links are being processed")
    public void correctGithubLinkTest(String link, String user, String repo) {
        request = new Request(link);
        assertEquals(
            new GitHubData(user, repo),
            handler.parse(request)
        );
    }

    @ParameterizedTest(name = "{index}: {0} not processed")
    @ValueSource(strings = {
        "https://github.com",
        "https://github.com/Alexander-1703/"
    })
    @DisplayName("a test that checks that invalid links to github are not processed")
    public void githubBadLinkTest(String link) {
        request = new Request(link);
        assertNull(handler.parse(request));
    }

    @ParameterizedTest(name = "{index}: {0} not processed")
    @ValueSource(strings = {
        "https://stackoverflow.com/questions/75736984/output-of-program-is-inccorect",
        "https://gitlab.com/questions/75736984/output-of-program-is-inccorect",
        "https://github.ru/questions/75736984/output-of-program-is-inccorect"
    })
    @DisplayName("a test that checks that a link not to a github is not processed")
    public void notGithubLinkTest(String link) {
        request = new Request(link);
        assertFalse(handler.defineLink(request.link()));
    }
}
