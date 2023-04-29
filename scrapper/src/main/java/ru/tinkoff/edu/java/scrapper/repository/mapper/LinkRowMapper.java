package ru.tinkoff.edu.java.scrapper.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import org.springframework.jdbc.core.RowMapper;

import ru.tinkoff.edu.java.scrapper.model.Link;

public class LinkRowMapper implements RowMapper<Link> {

    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong("id"));
        link.setLink(rs.getString("link"));
        link.setUpdatedAt(rs.getObject("updatedAt", OffsetDateTime.class));
        link.setGhForksCount(rs.getInt("ghForks"));
        link.setGhBranchesCount(rs.getInt("ghBranches"));
        link.setSoAnswersCount(rs.getInt("soAnswers"));
        return link;
    }
}
