import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  loginWithGoogle,
  logoutRequest,
  refreshAuth,
  validateAccessToken,
  type AuthUser,
} from '@/api/auth'
import { clearAccessToken, getAccessToken, setAccessToken } from '@/api/client'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<AuthUser | null>(null)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  function setSession(accessToken: string, nextUser: AuthUser) {
    setAccessToken(accessToken)
    user.value = nextUser
  }

  async function signInWithGoogle(credential: string) {
    const session = await loginWithGoogle(credential)
    setSession(session.accessToken, session.user)
  }

  // 'ok': 재발급 성공, 'none': refresh 토큰 없음, 'expired': refresh 토큰 만료/무효
  async function refresh(): Promise<'ok' | 'none' | 'expired'> {
    const session = await refreshAuth()
    if (typeof session === 'string') return session
    setSession(session.accessToken, session.user)
    return 'ok'
  }

  // 관리자 페이지 진입 판단: 'ok' 이동, 'login' 로그인 필요, 'forbidden' 권한 없음
  async function checkAdminAccess(): Promise<'ok' | 'login' | 'forbidden'> {
    let token = getAccessToken()  // 현재 저장된 access token 가져오기
    
    // access token이 없으면 refresh 시도
    if (!token) {
      if ((await refresh().catch(() => 'expired' as const)) !== 'ok') return 'login'
      token = getAccessToken()
    } else {
      // access token 유효성 검사
      const status = await validateAccessToken(token)

      // access token이 유효하지 않으면 refresh 시도, 실패하면 로그인 필요
      if (status < 200 || status >= 300) {
        if ((await refresh().catch(() => 'expired' as const)) !== 'ok') return 'login'

        // 재발급된 access token 가져오기, 재발급한거 바로 사용하므로 유효성 검사 불필요
        token = getAccessToken()
      }
    }

    return isAdmin.value ? 'ok' : 'forbidden'
  }

  function logout() {
    clearAccessToken()
    user.value = null
    void logoutRequest()
  }

  return { user, isAdmin, signInWithGoogle, refresh, checkAdminAccess, logout }
})
