package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import java.util.List;

import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkRepository {
    int add(Link link);

    int remove(long id);

    List<Link> findAll();
}
