package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;

@Service
@Slf4j
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (jdbcChatRepository.findById(tgChatId) != null) {
            log.info(tgChatId + " already registered");
        } else {
            jdbcChatRepository.add(tgChatId);
            log.info(tgChatId + " successfully registered");
        }
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        if (jdbcChatRepository.findById(tgChatId) == null) {
            log.info(tgChatId + " was not registered");
        } else {
            jdbcChatRepository.remove(tgChatId);
            log.info(tgChatId + " successfully unregistered");
        }
    }
}
