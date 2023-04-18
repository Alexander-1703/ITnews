package ru.tinkoff.edu.java.scrapper.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private Long id;
    private String link;
    private OffsetDateTime updatedAt;
    private int ghForksCount;
    private int ghBranchesCount;
    private int soAnswersCount;
}
