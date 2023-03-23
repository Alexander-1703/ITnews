package ru.tinkoff.edu.java.linkparser.handlers;

import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;

public interface Handler {

    boolean defineLink(String link);

    UrlData parse(Request request);
}
