let accessToken: string | null = null

export function setAccessToken(token: string | null) {
  accessToken = token
}

function requestWithToken(input: RequestInfo | URL, init: RequestInit): Promise<Response> {
  const headers = new Headers(init.headers)
  if (accessToken) headers.set('Authorization', `Bearer ${accessToken}`)
  return fetch(input, { ...init, headers })
}

let unauthorizedHandler: (() => void) | null = null

export function setUnauthorizedHandler(handler: () => void) {
  unauthorizedHandler = handler
}

export async function apiFetch(input: RequestInfo | URL, init: RequestInit = {}): Promise<Response> {
  const hadToken = accessToken !== null
  const response = await requestWithToken(input, init)
  // 토큰을 보냈는데 401이면 만료된 세션이므로 로그인 페이지로 보낸다.
  if (response.status === 401 && hadToken) unauthorizedHandler?.()
  return response
}

export function clearAccessToken() {
  accessToken = null
}
