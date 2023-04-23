package ru.tinkoff.edu.java.scrapper.service;

import java.net.URI;
import java.util.Collection;

import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkService {
    Link add(long tgChatId, URI url);
    Link remove(long tgChatId, URI url);
    Collection<Link> listAll(long tgChatId);
}
