package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import java.util.List;

import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkRepository {
    Link add(String link);

    boolean remove(long id);

    Link findById(long linkId);

    Link findByLink(String link);

    List<Link> findAll();
}
