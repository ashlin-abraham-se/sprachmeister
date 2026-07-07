import type { AnswerResponse, PhraseCreateRequest, PhraseQuestion } from './types'

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  })
  if (!response.ok) {
    const body = await response.text()
    throw new Error(`${response.status} ${response.statusText}${body ? `: ${body}` : ''}`)
  }
  return response.json() as Promise<T>
}

export function getRandomPhrase(): Promise<PhraseQuestion> {
  return request('/api/phrases/random')
}

export function submitAnswer(phraseId: number, answer: string): Promise<AnswerResponse> {
  return request(`/api/phrases/${phraseId}/answer`, {
    method: 'POST',
    body: JSON.stringify({ answer }),
  })
}

export function getAllPhrases(): Promise<PhraseQuestion[]> {
  return request('/api/phrases')
}

export function createPhrase(phrase: PhraseCreateRequest): Promise<PhraseQuestion> {
  return request('/api/phrases', {
    method: 'POST',
    body: JSON.stringify(phrase),
  })
}
