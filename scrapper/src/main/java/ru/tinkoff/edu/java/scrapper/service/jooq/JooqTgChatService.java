package ru.tinkoff.edu.java.scrapper.service.jooq;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@Service
@Slf4j
@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    private final JooqChatRepository chatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (chatRepository.findById(tgChatId) != null) {
            log.info(tgChatId + " already registered");
        } else {
            chatRepository.add(tgChatId);
            log.info(tgChatId + " successfully registered");
        }
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        if (chatRepository.findById(tgChatId) == null) {
            log.info(tgChatId + " was not registered");
        } else {
            chatRepository.remove(tgChatId);
            log.info(tgChatId + " successfully unregistered");
        }
    }
}
