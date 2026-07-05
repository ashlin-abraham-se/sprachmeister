package com.germanlearning.sprachmeister.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A single English phrase paired with one or more acceptable German
 * translations. Multiple acceptable answers are supported because German
 * often allows several valid ways to say the same thing
 * (e.g. word order, formal/informal "you").
 */
@Entity
@Table(name = "phrases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String english;

    /**
     * All translations considered correct for this phrase.
     * Stored in a separate collection table (phrase_answers).
     */
    @ElementCollection
    @CollectionTable(name = "phrase_answers", joinColumns = @JoinColumn(name = "phrase_id"))
    @Column(name = "answer", length = 500)
    private List<String> correctAnswers = new ArrayList<>();

    @Column(length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.MEDIUM;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
