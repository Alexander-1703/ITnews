package ru.tinkoff.edu.java.scrapper.repository;

import java.time.Duration;
import java.util.List;

import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkRepository {
    Link save(Link link);

    boolean remove(long id);

    Link findById(long linkId);

    Link findByLink(String link);

    List<Link> findNotUpdated(Duration interval);

    List<Link> findAll();
}
