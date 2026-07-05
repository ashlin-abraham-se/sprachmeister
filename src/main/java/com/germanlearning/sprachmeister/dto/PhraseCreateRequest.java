package com.germanlearning.sprachmeister.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PhraseCreateRequest {

    @NotBlank
    private String english;

    @NotEmpty(message = "At least one correct German translation is required")
    private List<String> correctAnswers;

    private String category;

    /** EASY, MEDIUM, or HARD. Defaults to MEDIUM if omitted/invalid. */
    private String difficulty;
}
