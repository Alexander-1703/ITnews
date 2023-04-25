package ru.tinkoff.edu.java.scrapper.repository.jooq;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import org.jooq.DSLContext;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Link.LINK;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext context;


    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            return context.insertInto(LINK)
                    .set(LINK.LINK_, link.getLink())
                    .returning(LINK.fields())
                    .fetchOneInto(Link.class);
        }
        return context.update(LINK)
                .set(LINK.LINK_, link.getLink())
                .set(LINK.UPDATEDAT, link.getUpdatedAt())
                .set(LINK.GHFORKS, link.getGhForksCount())
                .set(LINK.GHBRANCHES, link.getGhBranchesCount())
                .set(LINK.SOANSWERS, link.getSoAnswersCount())
                .returning(LINK.fields())
                .fetchOneInto(Link.class);
    }

    @Override
    public boolean remove(long id) {
        return context.deleteFrom(LINK)
                .where(LINK.ID.eq(id))
                .execute() > 0;
    }

    @Override
    public Link findById(long linkId) {
        return context.select(LINK.fields())
                .from(LINK)
                .where(LINK.ID.eq(linkId))
                .fetchOneInto(Link.class);
    }

    @Override
    public Link findByLink(String link) {
        return context.select(LINK.fields())
                .from(LINK)
                .where(LINK.LINK_.eq(link))
                .fetchOneInto(Link.class);
    }

    @Override
    public List<Link> findNotUpdated(Duration interval) {
        return context.select(LINK.fields())
                .from(LINK)
                .fetchInto(Link.class)
                .stream()
                .filter(link -> Duration.between(link.getUpdatedAt(), OffsetDateTime.now()).compareTo(interval) > 0)
                .toList();
    }

    @Override
    public List<Link> findAll() {
        return context.select(LINK.fields())
                .from(LINK)
                .fetchInto(Link.class);
    }
}
