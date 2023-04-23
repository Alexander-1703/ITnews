package ru.tinkoff.edu.java.scrapper.repository.jpa;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.tinkoff.edu.java.scrapper.model.Link;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Link findByLink(String link);

    List<Link> findAllByUpdatedAtBefore(OffsetDateTime dateTime);
}
