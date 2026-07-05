package com.germanlearning.sprachmeister.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerResponse {

    /** Overall verdict: CORRECT, CLOSE, or INCORRECT. */
    private String result;

    private String feedback;

    /** The user's submitted answer, echoed back. */
    private String submittedAnswer;

    /** All acceptable translations, shown when the user wasn't fully correct. */
    private List<String> correctAnswers;
}
