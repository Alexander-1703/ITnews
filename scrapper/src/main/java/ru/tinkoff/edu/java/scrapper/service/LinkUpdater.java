package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.UpdatedLink;
import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkUpdater {
    int update();

    UpdatedLink update(Link link);
}
