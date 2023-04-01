package ru.tinkoff.edu.java.bot.dto.request;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(Long id, URI uri, String description, List<Long> tgChatIds) {
}
