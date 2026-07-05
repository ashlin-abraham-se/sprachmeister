package com.germanlearning.sprachmeister.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * What the client sees when asked to translate a phrase.
 * Deliberately excludes the correct answers.
 */
@Getter
@AllArgsConstructor
public class PhraseQuestionDto {
    private Long id;
    private String english;
    private String category;
    private String difficulty;
}
