package com.germanlearning.sprachmeister.service;

import com.germanlearning.sprachmeister.dto.AnswerResponse;
import com.germanlearning.sprachmeister.dto.PhraseCreateRequest;
import com.germanlearning.sprachmeister.dto.PhraseQuestionDto;
import com.germanlearning.sprachmeister.model.Phrase;
import com.germanlearning.sprachmeister.repository.PhraseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Core game logic: hands out random phrases to translate and grades
 * submitted answers.
 *
 * Grading is deliberately forgiving: answers are normalized (case,
 * whitespace, punctuation) before comparison, and small typos are
 * tolerated via Levenshtein distance so a learner isn't marked wrong
 * for a single slipped key. Umlauts and ß are NOT folded away —
 * writing "schon" instead of "schön" is a real spelling mistake in
 * German, but it still counts as CLOSE rather than INCORRECT.
 */
@Service
public class PhraseService {

    public static final String RESULT_CORRECT = "CORRECT";
    public static final String RESULT_CLOSE = "CLOSE";
    public static final String RESULT_INCORRECT = "INCORRECT";

    /** One tolerated typo per this many characters of the correct answer. */
    private static final int CHARS_PER_ALLOWED_TYPO = 8;

    private final PhraseRepository phraseRepository;
    private final Random random = new Random();

    public PhraseService(PhraseRepository phraseRepository) {
        this.phraseRepository = phraseRepository;
    }

    /**
     * Picks one phrase at random. Uses count + a random page of size 1
     * so we never load the whole table just to choose a single row.
     */
    @Transactional(readOnly = true)
    public PhraseQuestionDto getRandomPhrase() {
        long count = phraseRepository.count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No phrases available yet");
        }
        int index = random.nextInt((int) count);
        Phrase phrase = phraseRepository.findAll(PageRequest.of(index, 1))
                .getContent()
                .get(0);
        return toQuestionDto(phrase);
    }

    @Transactional(readOnly = true)
    public List<PhraseQuestionDto> getAllPhrases() {
        return phraseRepository.findAll().stream()
                .map(this::toQuestionDto)
                .toList();
    }

    @Transactional
    public PhraseQuestionDto createPhrase(PhraseCreateRequest request) {
        Phrase phrase = new Phrase();
        phrase.setEnglish(request.getEnglish().trim());
        phrase.setCorrectAnswers(request.getCorrectAnswers().stream()
                .map(String::trim)
                .filter(a -> !a.isEmpty())
                .toList());
        phrase.setCategory(request.getCategory());
        phrase.setDifficulty(parseDifficulty(request.getDifficulty()));

        if (phrase.getCorrectAnswers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "At least one non-blank correct answer is required");
        }

        return toQuestionDto(phraseRepository.save(phrase));
    }

    /**
     * Grades a submitted answer against every acceptable translation and
     * keeps the best (lowest-distance) match:
     * distance 0 after normalization → CORRECT,
     * within the typo budget → CLOSE,
     * anything else → INCORRECT.
     */
    @Transactional(readOnly = true)
    public AnswerResponse checkAnswer(Long phraseId, String submittedAnswer) {
        Phrase phrase = phraseRepository.findById(phraseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Phrase " + phraseId + " not found"));

        String normalizedSubmission = normalize(submittedAnswer);

        int bestDistance = Integer.MAX_VALUE;
        String bestMatch = phrase.getCorrectAnswers().get(0);
        for (String correct : phrase.getCorrectAnswers()) {
            int distance = LevenshteinUtil.distance(normalizedSubmission, normalize(correct));
            if (distance < bestDistance) {
                bestDistance = distance;
                bestMatch = correct;
            }
        }

        if (bestDistance == 0) {
            return new AnswerResponse(RESULT_CORRECT, "Richtig! Well done.",
                    submittedAnswer, null);
        }
        if (bestDistance <= allowedTypos(bestMatch)) {
            return new AnswerResponse(RESULT_CLOSE,
                    "Almost! Check your spelling against: " + bestMatch,
                    submittedAnswer, phrase.getCorrectAnswers());
        }
        return new AnswerResponse(RESULT_INCORRECT,
                "Not quite. A correct translation is: " + bestMatch,
                submittedAnswer, phrase.getCorrectAnswers());
    }

    /**
     * Lowercases, strips punctuation, and collapses whitespace so that
     * "Wie geht's?" and "wie gehts" compare on the words alone.
     */
    private String normalize(String text) {
        return text.toLowerCase(Locale.GERMAN)
                .replaceAll("[.,!?;:'\"„“‚’«»-]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private int allowedTypos(String correctAnswer) {
        return Math.max(1, correctAnswer.length() / CHARS_PER_ALLOWED_TYPO);
    }

    private Phrase.Difficulty parseDifficulty(String raw) {
        if (raw == null) {
            return Phrase.Difficulty.MEDIUM;
        }
        try {
            return Phrase.Difficulty.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return Phrase.Difficulty.MEDIUM;
        }
    }

    private PhraseQuestionDto toQuestionDto(Phrase phrase) {
        return new PhraseQuestionDto(
                phrase.getId(),
                phrase.getEnglish(),
                phrase.getCategory(),
                phrase.getDifficulty().name()
        );
    }
}
