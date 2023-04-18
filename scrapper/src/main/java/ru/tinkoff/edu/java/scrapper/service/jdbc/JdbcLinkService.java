package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkUpdater;

@Service
@Slf4j
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final LinkChatRepository subscription;
    private final LinkUpdater linkUpdater;


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
                Link updatedLink = linkUpdater.update(link).getKey();
                linkRepository.save(updatedLink);
            } catch (NullPointerException e) {
                log.error("first updating error: " + url.toString());
            }
        }
        subscription.addLinkToChat(link.getId(), tgChatId);
        log.info("bind link " + link.getLink() + " to user " + tgChatId);
        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.findByLink(url.toString());
        subscription.removeLinkFromChat(link.getId(), tgChatId);
        log.info("unbind link " + link.getLink() + " from " + tgChatId);
        if (subscription.findChatsByLinkId(link.getId()).isEmpty()) {
            linkRepository.remove(link.getId());
            log.info("remove link: " + link.getLink());
        }
        return link;
    }

    @Override
    @Transactional
    public Collection<Link> listAll(long tgChatId) {
        return subscription.findLinksByChatId(tgChatId);
    }
}
