// Frontend mirrors of the backend DTOs
// (see com.germanlearning.sprachmeister.dto).

export type Difficulty = 'EASY' | 'MEDIUM' | 'HARD'

export type AnswerResult = 'CORRECT' | 'CLOSE' | 'INCORRECT'

export interface PhraseQuestion {
  id: number
  english: string
  category: string | null
  difficulty: Difficulty
}

export interface AnswerResponse {
  result: AnswerResult
  feedback: string
  submittedAnswer: string
  /** Populated only when the answer wasn't fully correct. */
  correctAnswers: string[] | null
}

export interface PhraseCreateRequest {
  english: string
  correctAnswers: string[]
  category?: string
  difficulty?: Difficulty
}
