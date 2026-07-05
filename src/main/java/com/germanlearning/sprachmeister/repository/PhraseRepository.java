package com.germanlearning.sprachmeister.repository;

import com.germanlearning.sprachmeister.model.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {
}
