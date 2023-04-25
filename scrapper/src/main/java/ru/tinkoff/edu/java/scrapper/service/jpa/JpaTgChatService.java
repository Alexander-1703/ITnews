package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@Slf4j
@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            log.info(tgChatId + " already registered");
        } else {
            Chat chat = new Chat();
            chat.setId(tgChatId);

            chatRepository.save(chat);
            log.info(tgChatId + " successfully registered");
        }
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            log.info(tgChatId + " was not registered");
        } else {
            chatRepository.deleteById(tgChatId);
            log.info(tgChatId + " successfully unregistered");
        }
    }
}
