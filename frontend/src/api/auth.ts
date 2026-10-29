export const GOOGLE_AUTH_STORAGE_KEY = 'google_auth'

export interface AuthUser {
  email: string
  name: string
  role: 'USER' | 'ADMIN'
}

export interface AuthResponse {
  accessToken: string
  tokenType: 'Bearer'
  expiresIn: number
  user: AuthUser
}

export async function loginWithGoogle(credential: string): Promise<AuthResponse> {
  const response = await fetch('/api/v1/auth/google', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'same-origin',
    body: JSON.stringify({ credential }),
  })
  if (!response.ok) {
    throw new Error(`Google 로그인에 실패했습니다. (${response.status})`)
  }
  return response.json() as Promise<AuthResponse>
}

// refresh 토큰은 HttpOnly 쿠키로 전달된다. 만료/없음(401)이면 null.
export async function refreshAuth(): Promise<AuthResponse | null> {
  const response = await fetch('/api/v1/auth/refresh', {
    method: 'POST',
    credentials: 'same-origin',
  })
  if (!response.ok) return null
  return response.json() as Promise<AuthResponse>
}

export async function logoutRequest(): Promise<void> {
  await fetch('/api/v1/auth/logout', { method: 'POST', credentials: 'same-origin' }).catch(() => {})
}
