package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;

@RequiredArgsConstructor
public class JpaSubscriptionService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    public List<Chat> findChatsByLinkId(long linkId) {
        Optional<Link> linkOptional = linkRepository.findByIdWithChats(linkId);

        if (linkOptional.isPresent()) {
            return new ArrayList<>(linkOptional.get().getChats());
        }
        return Collections.emptyList();
    }

    public List<Link> findLinksByChatId(long chatId) {
        Optional<Chat> chatOptional = chatRepository.findByIdWithLinks(chatId);

        if (chatOptional.isPresent()) {
            return new ArrayList<>(chatOptional.get().getLinks());
        }
        return Collections.emptyList();
    }

    public boolean addLinkToChat(long linkId, long chatId) {
        Optional<Link> optionalLink = linkRepository.findById(linkId);
        Optional<Chat> optionalChat = chatRepository.findById(chatId);

        if (optionalLink.isPresent() && optionalChat.isPresent()) {
            Link link = optionalLink.get();
            Chat chat = optionalChat.get();
            if (!chat.getLinks().contains(link)) {
                chat.getLinks().add(link);
                chatRepository.save(chat);
                return true;
            }
        }
        return false;
    }

    public boolean removeLinkFromChat(long linkId, long chatId) {
        Optional<Link> optionalLink = linkRepository.findById(linkId);
        Optional<Chat> optionalChat = chatRepository.findById(chatId);

        if (optionalLink.isPresent() && optionalChat.isPresent()) {
            Link link = optionalLink.get();
            Chat chat = optionalChat.get();
            return chat.getLinks().remove(link);
        }
        return false;
    }
}
