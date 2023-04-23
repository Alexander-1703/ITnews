package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@Service
@Slf4j
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final JpaLinkUpdater linkUpdater;

    @Override
    @Transactional
    public Link add(long tgChatId, URI url) {
        Link link = linkRepository.findByLink(url.toString());
        if (link == null) {
            link = new Link();
            link.setLink(url.toString());

            link = linkRepository.save(link);
            log.info("add link: " + link.getLink());

            try {
                Link updatedLink = linkUpdater.update(link).link();
                linkRepository.save(updatedLink);

            } catch (NullPointerException e) {
                log.error("first updating error: " + url);
            }
        }
        addLinkToChat(link.getId(), tgChatId);
        log.info("bind link " + link.getLink() + " to user " + tgChatId);
        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.findByLink(url.toString());
        removeLinkFromChat(link.getId(), tgChatId);
        log.info("unbind link " + link.getLink() + " from " + tgChatId);
        if (findChatsByLinkId(link.getId()).isEmpty()) {
            linkRepository.deleteById(link.getId());
            log.info("remove link: " + link.getLink());
        }
        return link;
    }

    @Override
    @Transactional
    public Collection<Link> listAll(long tgChatId) {
        return findLinksByChatId(tgChatId);
    }

    public List<Chat> findChatsByLinkId(long linkId) {
        Optional<Link> linkOptional = linkRepository.findById(linkId);

        if (linkOptional.isPresent()) {
            return new ArrayList<>(linkOptional.get().getChats());
        }
        return Collections.emptyList();
    }

    public List<Link> findLinksByChatId(long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);

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
