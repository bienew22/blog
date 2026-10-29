<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { GOOGLE_AUTH_STORAGE_KEY } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

function readIdTokenNonce(idToken: string): string | null {
  try {
    const payload = idToken.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(atob(payload)).nonce ?? null
  } catch {
    return null
  }
}

function fail() {
  void router.replace({ name: 'login', query: { error: '1' } })
}

onMounted(async () => {
  const params = new URLSearchParams(window.location.hash.slice(1))
  const idToken = params.get('id_token')
  const stored = sessionStorage.getItem(GOOGLE_AUTH_STORAGE_KEY)
  sessionStorage.removeItem(GOOGLE_AUTH_STORAGE_KEY)

  if (!idToken || !stored) return fail()
  const { state, nonce, redirect } = JSON.parse(stored) as {
    state: string
    nonce: string
    redirect: string
  }
  if (params.get('state') !== state || readIdTokenNonce(idToken) !== nonce) return fail()

  try {
    await authStore.signInWithGoogle(idToken)
  } catch {
    return fail()
  }
  const safeRedirect = redirect.startsWith('/') && !redirect.startsWith('//') ? redirect : '/'
  void router.replace(safeRedirect)
})
</script>

<template>
  <main class="flex min-h-screen items-center justify-center bg-(--color-background) text-sm text-(--color-text-secondary)">
    로그인 중...
  </main>
</template>
