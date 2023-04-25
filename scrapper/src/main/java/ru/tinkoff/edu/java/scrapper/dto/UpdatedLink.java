package ru.tinkoff.edu.java.scrapper.dto;

import ru.tinkoff.edu.java.scrapper.model.Link;

public record UpdatedLink(Link link, String description) {
}
