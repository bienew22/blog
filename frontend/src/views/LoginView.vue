<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { GOOGLE_AUTH_STORAGE_KEY } from '@/api/auth'

const route = useRoute()
const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID
const error = ref(
  googleClientId
    ? route.query.error
      ? '로그인에 실패했습니다. 다시 시도해 주세요.'
      : ''
    : '현재 Google 로그인을 이용할 수 없습니다. 잠시 후 다시 시도해 주세요.',
)

function getRedirectPath(): string {
  const redirect = route.query.redirect
  return typeof redirect === 'string' && redirect.startsWith('/') && !redirect.startsWith('//')
    ? redirect
    : '/'
}

function signInWithGoogle() {
  if (!googleClientId) return
  const state = crypto.randomUUID()
  const nonce = crypto.randomUUID()
  sessionStorage.setItem(
    GOOGLE_AUTH_STORAGE_KEY,
    JSON.stringify({ state, nonce, redirect: getRedirectPath() }),
  )

  const params = new URLSearchParams({
    client_id: googleClientId,
    redirect_uri: `${window.location.origin}/login/callback`,
    response_type: 'id_token',
    response_mode: 'fragment',
    scope: 'openid email profile',
    prompt: 'select_account',
    state,
    nonce,
  })
  window.location.assign(`https://accounts.google.com/o/oauth2/v2/auth?${params}`)
}
</script>

<template>
  <main class="flex min-h-screen items-center justify-center bg-(--color-background) px-6">
    <section class="w-full max-w-md rounded-2xl border border-(--color-border) bg-(--color-surface) p-8 text-center shadow-sm">
      <h1 class="text-2xl font-bold text-(--color-heading)">로그인</h1>
      <p class="mt-3 text-sm text-(--color-text-secondary)">다음 계정으로 로그인하세요.</p>
      <button
        type="button"
        :disabled="!googleClientId"
        class="mt-8 inline-flex h-11 w-70 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-sm font-medium text-(--color-text-secondary) transition-colors hover:cursor-pointer hover:border-(--color-primary) hover:text-(--color-primary) disabled:cursor-not-allowed disabled:opacity-50"
        @click="signInWithGoogle"
      >
        Google 계정으로 로그인
      </button>
      <p v-if="error" role="alert" class="mt-4 text-sm text-red-600">{{ error }}</p>
      <RouterLink to="/" class="mt-8 inline-block text-sm text-(--color-primary) hover:underline">
        블로그로 돌아가기
      </RouterLink>
    </section>
  </main>
</template>
