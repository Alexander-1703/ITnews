package ru.tinkoff.edu.java.scrapper.repository.jpa;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.tinkoff.edu.java.scrapper.model.Chat;

@Repository
@ConditionalOnProperty(prefix = "scrapper", name = "accessType", havingValue = "jpa", matchIfMissing = true)
public interface JpaChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN FETCH c.links WHERE c.id = :chatId")
    Optional<Chat> findByIdWithLinks(long chatId);
}
