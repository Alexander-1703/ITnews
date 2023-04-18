package ru.tinkoff.edu.java.scrapper.service.interfaces;

import org.apache.commons.lang3.tuple.Pair;

import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkUpdater {
    int update();

    Pair<Link, String> update(Link link);
}
