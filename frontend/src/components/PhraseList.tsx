import { useEffect, useState } from 'react'
import { getAllPhrases } from '../api'
import type { PhraseQuestion } from '../types'
import AddPhraseForm from './AddPhraseForm'

/** Browse the phrase pool and add new phrases to it. */
export default function PhraseList() {
  const [phrases, setPhrases] = useState<PhraseQuestion[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    getAllPhrases()
      .then(setPhrases)
      .catch((e) => setError(e instanceof Error ? e.message : 'Failed to load phrases'))
  }, [])

  return (
    <section>
      <AddPhraseForm onCreated={(p) => setPhrases((list) => [...list, p])} />

      {error && <p className="error">{error}</p>}

      <div className="card">
        <h2>Phrase pool ({phrases.length})</h2>
        <table className="phrase-table">
          <thead>
            <tr>
              <th>English</th>
              <th>Category</th>
              <th>Difficulty</th>
            </tr>
          </thead>
          <tbody>
            {phrases.map((p) => (
              <tr key={p.id}>
                <td>{p.english}</td>
                <td>{p.category ?? '—'}</td>
                <td>
                  <span className={`badge difficulty-${p.difficulty.toLowerCase()}`}>
                    {p.difficulty}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  )
}
