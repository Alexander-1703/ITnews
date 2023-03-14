package ru.tinkoff.edu.java.linkparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverflowHandler extends AbstractHandler {

    private static final int INDEX_SHIFT = 10;

    public StackOverflowHandler(AbstractHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handleRequest(Request request) {
        if (request == null) {
            return;
        }

        if (defineLink(request.link())) {
            System.out.println(getId(request.link()));
            return;
        }

        if (nextHandler != null) {
            nextHandler.handleRequest(request);
            return;
        }
        System.out.println("Некорректная ссылка");
    }

    @Override
    protected boolean defineLink(String link) {
        String regex = "^(https?://)?(www\\.)?stackoverflow\\.com(/.*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(link).find();
    }

    public long getId(String link) {
        Pattern pattern = Pattern.compile("questions/\\d+");
        Matcher matcher = pattern.matcher(link);
        int startIndex = 0;
        int endIndex = 0;
        if (matcher.find()) {
            startIndex = matcher.start() + INDEX_SHIFT;
            endIndex = matcher.end();
        }

        if (startIndex != endIndex) {
            return Long.parseLong(link.substring(startIndex, endIndex));
        } else {
            return -1;
        }
    }
}
