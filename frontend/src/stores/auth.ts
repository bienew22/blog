import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { loginWithGoogle, type AuthUser } from '@/api/auth'
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

  function logout() {
    clearAccessToken()
    user.value = null
  }

  return { user, isAdmin, signInWithGoogle, logout }
})
