let accessToken: string | null = null

export function setAccessToken(token: string | null) {
  accessToken = token
}

function requestWithToken(input: RequestInfo | URL, init: RequestInit): Promise<Response> {
  const headers = new Headers(init.headers)
  if (accessToken) headers.set('Authorization', `Bearer ${accessToken}`)
  return fetch(input, { ...init, headers })
}

export function apiFetch(input: RequestInfo | URL, init: RequestInit = {}): Promise<Response> {
  return requestWithToken(input, init)
}

export function clearAccessToken() {
  accessToken = null
}
