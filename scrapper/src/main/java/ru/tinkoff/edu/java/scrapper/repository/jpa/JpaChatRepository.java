package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.tinkoff.edu.java.scrapper.model.Chat;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
