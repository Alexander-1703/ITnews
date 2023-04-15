package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.net.URI;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

@Service
@Slf4j
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcLinkChatRepository subscription;


    @Override
    @Transactional
    public Link add(long tgChatId, URI url) {
        Link link = jdbcLinkRepository.findByLink(url.toString());
        if (link == null) {
            link = jdbcLinkRepository.add(url.toString());
            log.info("add link: " + link.getLink());
        }
        subscription.addLinkToChat(link.getId(), tgChatId);
        log.info("bind link " + link.getLink() + " to user " + tgChatId);
        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        Link link = jdbcLinkRepository.findByLink(url.toString());
        subscription.removeLinkFromChat(link.getId(), tgChatId);
        log.info("unbind link " + link.getLink() + " from " + tgChatId);
        if (subscription.findChatsByLinkId(link.getId()).isEmpty()) {
            jdbcLinkRepository.remove(link.getId());
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
