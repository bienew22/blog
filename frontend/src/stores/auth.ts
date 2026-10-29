import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { loginWithGoogle, logoutRequest, refreshAuth, type AuthUser } from '@/api/auth'
import { clearAccessToken, setAccessToken } from '@/api/client'

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

  // 성공 시 true, refresh 토큰이 없거나 만료되면 false
  async function refresh(): Promise<boolean> {
    const session = await refreshAuth()
    if (!session) return false
    setSession(session.accessToken, session.user)
    return true
  }

  function logout() {
    clearAccessToken()
    user.value = null
    void logoutRequest()
  }

  return { user, isAdmin, signInWithGoogle, refresh, logout }
})
