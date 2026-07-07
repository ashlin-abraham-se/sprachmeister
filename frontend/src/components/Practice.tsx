import { useCallback, useEffect, useRef, useState } from 'react'
import { getRandomPhrase, submitAnswer } from '../api'
import type { AnswerResponse, PhraseQuestion } from '../types'

interface Score {
  correct: number
  close: number
  incorrect: number
}

/**
 * The core practice loop: show a random English phrase, grade the
 * user's German translation, then move on to the next phrase.
 */
export default function Practice() {
  const [phrase, setPhrase] = useState<PhraseQuestion | null>(null)
  const [answer, setAnswer] = useState('')
  const [result, setResult] = useState<AnswerResponse | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const [score, setScore] = useState<Score>({ correct: 0, close: 0, incorrect: 0 })
  const inputRef = useRef<HTMLInputElement>(null)

  const nextPhrase = useCallback(async () => {
    setError(null)
    setResult(null)
    setAnswer('')
    setLoading(true)
    try {
      setPhrase(await getRandomPhrase())
      inputRef.current?.focus()
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load phrase')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void nextPhrase()
  }, [nextPhrase])

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    if (!phrase || !answer.trim() || result) return
    setError(null)
    setLoading(true)
    try {
      const graded = await submitAnswer(phrase.id, answer)
      setResult(graded)
      setScore((s) => ({
        correct: s.correct + (graded.result === 'CORRECT' ? 1 : 0),
        close: s.close + (graded.result === 'CLOSE' ? 1 : 0),
        incorrect: s.incorrect + (graded.result === 'INCORRECT' ? 1 : 0),
      }))
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to submit answer')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="practice">
      <div className="scoreboard" aria-label="session score">
        <span className="score-pill correct">{score.correct} correct</span>
        <span className="score-pill close">{score.close} close</span>
        <span className="score-pill incorrect">{score.incorrect} wrong</span>
      </div>

      {error && <p className="error">{error}</p>}

      {phrase && (
        <div className="card">
          <div className="phrase-meta">
            <span className={`badge difficulty-${phrase.difficulty.toLowerCase()}`}>
              {phrase.difficulty}
            </span>
            {phrase.category && <span className="badge category">{phrase.category}</span>}
          </div>

          <p className="prompt-label">Translate into German:</p>
          <h2 className="english-phrase">{phrase.english}</h2>

          <form onSubmit={handleSubmit}>
            <input
              ref={inputRef}
              type="text"
              value={answer}
              onChange={(e) => setAnswer(e.target.value)}
              placeholder="Deine Übersetzung…"
              disabled={loading || result !== null}
              autoFocus
              lang="de"
              autoComplete="off"
              autoCapitalize="off"
              spellCheck={false}
            />
            {!result && (
              <button type="submit" disabled={loading || !answer.trim()}>
                Check answer
              </button>
            )}
          </form>

          {result && (
            <div className={`result result-${result.result.toLowerCase()}`}>
              <p className="verdict">{result.result}</p>
              <p>{result.feedback}</p>
              {result.correctAnswers && (
                <ul className="answers">
                  {result.correctAnswers.map((a) => (
                    <li key={a}>{a}</li>
                  ))}
                </ul>
              )}
              <button onClick={() => void nextPhrase()} autoFocus>
                Next phrase →
              </button>
            </div>
          )}
        </div>
      )}
    </section>
  )
}
