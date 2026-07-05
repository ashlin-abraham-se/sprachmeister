package com.germanlearning.sprachmeister.controller;

import com.germanlearning.sprachmeister.dto.AnswerRequest;
import com.germanlearning.sprachmeister.dto.AnswerResponse;
import com.germanlearning.sprachmeister.dto.PhraseCreateRequest;
import com.germanlearning.sprachmeister.dto.PhraseQuestionDto;
import com.germanlearning.sprachmeister.service.PhraseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST surface for the translation-practice loop:
 * fetch a random phrase, submit an answer, browse/add phrases.
 */
@RestController
@RequestMapping("/api/phrases")
public class PhraseController {

    private final PhraseService phraseService;

    public PhraseController(PhraseService phraseService) {
        this.phraseService = phraseService;
    }

    /** A random phrase to translate (correct answers withheld). */
    @GetMapping("/random")
    public PhraseQuestionDto getRandomPhrase() {
        return phraseService.getRandomPhrase();
    }

    /** Submit a German translation for the given phrase and get it graded. */
    @PostMapping("/{id}/answer")
    public AnswerResponse submitAnswer(@PathVariable Long id,
                                       @Valid @RequestBody AnswerRequest request) {
        return phraseService.checkAnswer(id, request.getAnswer());
    }

    /** All phrases in the pool (still without their answers). */
    @GetMapping
    public List<PhraseQuestionDto> getAllPhrases() {
        return phraseService.getAllPhrases();
    }

    /** Add a new phrase with one or more acceptable translations. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhraseQuestionDto createPhrase(@Valid @RequestBody PhraseCreateRequest request) {
        return phraseService.createPhrase(request);
    }
}
