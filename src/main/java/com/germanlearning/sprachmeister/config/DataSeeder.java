package com.germanlearning.sprachmeister.config;

import com.germanlearning.sprachmeister.model.Phrase;
import com.germanlearning.sprachmeister.repository.PhraseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Seeds the (in-memory) database with a starter set of English phrases
 * and their acceptable German translations, so the app is usable
 * immediately after startup with no manual data entry.
 *
 * Add your own phrases here as you learn new vocabulary/grammar,
 * or use the POST /api/phrases endpoint once the app is running.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final PhraseRepository phraseRepository;

    public DataSeeder(PhraseRepository phraseRepository) {
        this.phraseRepository = phraseRepository;
    }

    @Override
    public void run(String... args) {
        if (phraseRepository.count() > 0) {
            return; // already seeded
        }

        List<Phrase> starterPhrases = Arrays.asList(
                phrase("Good morning", Phrase.Difficulty.EASY, "greetings", "Guten Morgen"),
                phrase("Thank you very much", Phrase.Difficulty.EASY, "greetings", "Vielen Dank", "Danke sehr", "Danke schön"),
                phrase("How are you?", Phrase.Difficulty.EASY, "greetings", "Wie geht es dir?", "Wie geht's?", "Wie geht es Ihnen?"),
                phrase("My name is Anna", Phrase.Difficulty.EASY, "greetings", "Ich heiße Anna", "Mein Name ist Anna"),
                phrase("Where is the train station?", Phrase.Difficulty.EASY, "travel", "Wo ist der Bahnhof?"),
                phrase("I would like a coffee, please", Phrase.Difficulty.EASY, "food", "Ich hätte gerne einen Kaffee, bitte", "Ich möchte einen Kaffee, bitte"),
                phrase("The weather is nice today", Phrase.Difficulty.EASY, "small talk", "Das Wetter ist heute schön", "Heute ist das Wetter schön"),
                phrase("I don't understand", Phrase.Difficulty.EASY, "small talk", "Ich verstehe nicht", "Ich verstehe das nicht"),
                phrase("Can you help me, please?", Phrase.Difficulty.EASY, "small talk", "Können Sie mir bitte helfen?", "Kannst du mir bitte helfen?"),
                phrase("I am learning German", Phrase.Difficulty.EASY, "small talk", "Ich lerne Deutsch"),

                phrase("I have been living in Berlin for three years", Phrase.Difficulty.MEDIUM, "daily life", "Ich lebe seit drei Jahren in Berlin", "Ich wohne seit drei Jahren in Berlin"),
                phrase("She goes to work by bike every day", Phrase.Difficulty.MEDIUM, "daily life", "Sie fährt jeden Tag mit dem Fahrrad zur Arbeit"),
                phrase("We are going to the cinema tomorrow evening", Phrase.Difficulty.MEDIUM, "daily life", "Wir gehen morgen Abend ins Kino"),
                phrase("He forgot his umbrella at home", Phrase.Difficulty.MEDIUM, "daily life", "Er hat seinen Regenschirm zu Hause vergessen"),
                phrase("Could you speak more slowly, please?", Phrase.Difficulty.MEDIUM, "small talk", "Könnten Sie bitte langsamer sprechen?", "Kannst du bitte langsamer sprechen?"),
                phrase("I need to renew my passport", Phrase.Difficulty.MEDIUM, "bureaucracy", "Ich muss meinen Reisepass verlängern lassen", "Ich muss meinen Pass verlängern"),
                phrase("The meeting has been postponed to next week", Phrase.Difficulty.MEDIUM, "work", "Das Meeting wurde auf nächste Woche verschoben", "Die Besprechung wurde auf nächste Woche verschoben"),
                phrase("I am looking forward to the weekend", Phrase.Difficulty.MEDIUM, "small talk", "Ich freue mich auf das Wochenende"),
                phrase("Unfortunately, I don't have time today", Phrase.Difficulty.MEDIUM, "small talk", "Leider habe ich heute keine Zeit"),
                phrase("Please fill out this form completely", Phrase.Difficulty.MEDIUM, "bureaucracy", "Bitte füllen Sie dieses Formular vollständig aus"),

                phrase("If I had known that, I would have come earlier", Phrase.Difficulty.HARD, "grammar", "Wenn ich das gewusst hätte, wäre ich früher gekommen"),
                phrase("Although it was raining, we went for a walk", Phrase.Difficulty.HARD, "grammar", "Obwohl es regnete, sind wir spazieren gegangen", "Obwohl es geregnet hat, sind wir spazieren gegangen"),
                phrase("The book I am currently reading is very exciting", Phrase.Difficulty.HARD, "grammar", "Das Buch, das ich gerade lese, ist sehr spannend"),
                phrase("She would rather work from home than commute", Phrase.Difficulty.HARD, "grammar", "Sie würde lieber von zu Hause aus arbeiten, als zu pendeln"),
                phrase("As soon as I arrive, I will call you", Phrase.Difficulty.HARD, "grammar", "Sobald ich ankomme, rufe ich dich an"),
                phrase("Despite the difficulties, the project was completed on time", Phrase.Difficulty.HARD, "work", "Trotz der Schwierigkeiten wurde das Projekt rechtzeitig fertiggestellt"),
                phrase("Not only did he arrive late, but he also forgot the documents", Phrase.Difficulty.HARD, "grammar", "Er kam nicht nur zu spät, sondern vergaß auch die Unterlagen"),
                phrase("It is said that the company will be sold next year", Phrase.Difficulty.HARD, "grammar", "Es heißt, dass die Firma nächstes Jahr verkauft wird"),
                phrase("The more I practice, the better I get", Phrase.Difficulty.HARD, "grammar", "Je mehr ich übe, desto besser werde ich"),
                phrase("Had I known about the delay, I would have taken another train", Phrase.Difficulty.HARD, "grammar", "Hätte ich von der Verspätung gewusst, hätte ich einen anderen Zug genommen")
        );

        phraseRepository.saveAll(starterPhrases);
    }

    private Phrase phrase(String english, Phrase.Difficulty difficulty, String category, String... answers) {
        Phrase p = new Phrase();
        p.setEnglish(english);
        p.setDifficulty(difficulty);
        p.setCategory(category);
        p.setCorrectAnswers(Arrays.asList(answers));
        return p;
    }
}
