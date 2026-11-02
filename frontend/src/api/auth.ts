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

// 204: 유효, 401: access token 만료/무효, 0: 네트워크 오류
export async function validateAccessToken(token: string): Promise<number> {
  try {
    const response = await fetch('/api/v1/auth/validate', {
      headers: { Authorization: 'Bearer ' + token },
    })
    return response.status
  } catch {
    return 0
  }
}

// refresh 토큰은 HttpOnly 쿠키로 전달된다. 쿠키 없음(204)이면 'none', 만료/무효(401)이면 'expired'.
export async function refreshAuth(): Promise<AuthResponse | 'none' | 'expired'> {
  const response = await fetch('/api/v1/auth/refresh', {
    method: 'POST',
    credentials: 'same-origin',
  })
  if (response.status === 204) return 'none'
  if (!response.ok) return 'expired'
  return response.json() as Promise<AuthResponse>
}

export async function logoutRequest(): Promise<void> {
  await fetch('/api/v1/auth/logout', { method: 'POST', credentials: 'same-origin' }).catch(() => {})
}
