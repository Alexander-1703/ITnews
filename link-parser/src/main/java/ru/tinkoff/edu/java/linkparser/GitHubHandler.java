package ru.tinkoff.edu.java.linkparser;

import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubHandler extends AbstractHandler {
    private static final int INDEX_SHIFT = 11;

    public GitHubHandler(AbstractHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public Object handleRequest(Request request) {
        if (request == null) {
            return null;
        }

        if (defineLink(request.link())) {
            return parse(request.link());
        }

        if (nextHandler != null) {
            return nextHandler.handleRequest(request);
        }
        return null;
    }

    @Override
    protected boolean defineLink(String link) {
        String regex = "^(https?://)?(www\\.)?github\\.com(/.*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(link).find();
    }

    @Override
    public AbstractMap.SimpleEntry<String, String> parse(String link) {
        Pattern pattern = Pattern.compile("github\\.com/([^/]+)/([^/]+)");
        Matcher matcher = pattern.matcher(link);
        int startIndex = 0;
        int endIndex = 0;
        if (matcher.find()) {
            startIndex = matcher.start() + INDEX_SHIFT;
            endIndex = matcher.end();
        }

        if (startIndex == endIndex) {
            return null;
        }

        String userAndRep = link.substring(startIndex, endIndex);
        return new AbstractMap.SimpleEntry<>(userAndRep.split("/")[0], userAndRep.split("/")[1]);
    }
}
