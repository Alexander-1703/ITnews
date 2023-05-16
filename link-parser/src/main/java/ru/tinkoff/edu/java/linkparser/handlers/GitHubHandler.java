package ru.tinkoff.edu.java.linkparser.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;

public class GitHubHandler extends AbstractHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubHandler.class);
    private static final String REGEX_PATTERN_GITHUB = "^(https?://)?(www\\.)?github\\.com(/.*)$";
    private static final int MAX_PATH_COMPONENTS = 3;

    public GitHubHandler(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean defineLink(String link) {
        return Pattern.compile(REGEX_PATTERN_GITHUB).matcher(link).find();
    }

    @Override
    public UrlData parse(Request request) {
        if (!defineLink(request.link())) {
            return nextHandler == null ? null : nextHandler.parse(request);
        }
        try {
            URI uri = new URI(request.link());
            String[] pathComponents = uri.getPath().split("/");
            if (pathComponents.length >= MAX_PATH_COMPONENTS) {
                return new GitHubData(pathComponents[1], pathComponents[2]);
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Error parsing link: {}", e.getMessage());
        }
        return null;
    }
}
