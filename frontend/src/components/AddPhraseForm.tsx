import { useState } from 'react'
import { createPhrase } from '../api'
import type { Difficulty, PhraseQuestion } from '../types'

interface Props {
  onCreated: (phrase: PhraseQuestion) => void
}

/**
 * Form for adding a phrase. Multiple acceptable translations are
 * entered one per line, matching the backend's List<String> answers.
 */
export default function AddPhraseForm({ onCreated }: Props) {
  const [english, setEnglish] = useState('')
  const [answers, setAnswers] = useState('')
  const [category, setCategory] = useState('')
  const [difficulty, setDifficulty] = useState<Difficulty>('MEDIUM')
  const [error, setError] = useState<string | null>(null)
  const [saved, setSaved] = useState(false)

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    setError(null)
    setSaved(false)

    const correctAnswers = answers
      .split('\n')
      .map((a) => a.trim())
      .filter(Boolean)

    if (!english.trim() || correctAnswers.length === 0) {
      setError('English phrase and at least one German translation are required.')
      return
    }

    try {
      const created = await createPhrase({
        english: english.trim(),
        correctAnswers,
        category: category.trim() || undefined,
        difficulty,
      })
      onCreated(created)
      setEnglish('')
      setAnswers('')
      setCategory('')
      setSaved(true)
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to save phrase')
    }
  }

  return (
    <div className="card">
      <h2>Add a phrase</h2>
      <form className="add-form" onSubmit={handleSubmit}>
        <label>
          English phrase
          <input
            type="text"
            value={english}
            onChange={(e) => setEnglish(e.target.value)}
            placeholder="e.g. I am hungry"
          />
        </label>
        <label>
          Accepted German translations (one per line)
          <textarea
            value={answers}
            onChange={(e) => setAnswers(e.target.value)}
            placeholder={'Ich habe Hunger\nIch bin hungrig'}
            rows={3}
            lang="de"
          />
        </label>
        <div className="form-row">
          <label>
            Category
            <input
              type="text"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              placeholder="e.g. food"
            />
          </label>
          <label>
            Difficulty
            <select
              value={difficulty}
              onChange={(e) => setDifficulty(e.target.value as Difficulty)}
            >
              <option value="EASY">Easy</option>
              <option value="MEDIUM">Medium</option>
              <option value="HARD">Hard</option>
            </select>
          </label>
        </div>
        <button type="submit">Add phrase</button>
        {saved && <span className="saved-note">Saved ✓</span>}
        {error && <p className="error">{error}</p>}
      </form>
    </div>
  )
}
