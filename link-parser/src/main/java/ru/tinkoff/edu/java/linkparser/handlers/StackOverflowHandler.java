package ru.tinkoff.edu.java.linkparser.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;

public class StackOverflowHandler extends AbstractHandler {

    public StackOverflowHandler(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean defineLink(String link) {
        String regex = "^(https?://)?(www\\.)?stackoverflow\\.com/questions(/.*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(link).find();
    }

    @Override
    public UrlData parse(Request request) {
        if (!defineLink(request.link())) {
            return nextHandler == null ? null :  nextHandler.parse(request);
        }
        try {
            URI uri = new URI(request.link());
            String[] pathComponents = uri.getPath().split("/");
            if (pathComponents.length >= 3) {
                return new StackOverflowData(Long.parseLong(pathComponents[2]));
            }
        } catch (URISyntaxException | NumberFormatException e) {
            System.err.println("Invalid url: " + request.link());
        }
        return null;
    }
}
