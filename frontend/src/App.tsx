import { useState } from 'react'
import Practice from './components/Practice'
import PhraseList from './components/PhraseList'
import './App.css'

type Tab = 'practice' | 'phrases'

export default function App() {
  const [tab, setTab] = useState<Tab>('practice')

  return (
    <div className="app">
      <header>
        <h1>Sprachmeister</h1>
        <p className="tagline">Practice your German translations</p>
        <nav>
          <button
            className={tab === 'practice' ? 'active' : ''}
            onClick={() => setTab('practice')}
          >
            Practice
          </button>
          <button
            className={tab === 'phrases' ? 'active' : ''}
            onClick={() => setTab('phrases')}
          >
            Phrases
          </button>
        </nav>
      </header>

      <main>{tab === 'practice' ? <Practice /> : <PhraseList />}</main>
    </div>
  )
}
