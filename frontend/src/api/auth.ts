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
