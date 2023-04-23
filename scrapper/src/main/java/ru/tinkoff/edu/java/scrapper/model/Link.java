package ru.tinkoff.edu.java.scrapper.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "link")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue
    private Long id;

    private String link;

    private OffsetDateTime updatedAt;

    private Integer ghForksCount;

    private Integer ghBranchesCount;

    private Integer soAnswersCount;

    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();
}
