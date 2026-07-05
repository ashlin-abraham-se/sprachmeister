package com.germanlearning.sprachmeister.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequest {

    @NotBlank(message = "answer must not be blank")
    private String answer;
}
