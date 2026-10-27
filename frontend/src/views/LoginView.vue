<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const button = ref<HTMLElement | null>(null)
const error = ref('')
const isLoading = ref(false)
const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID
let script: HTMLScriptElement | null = null

function getRedirectPath(): string {
  const redirect = route.query.redirect
  return typeof redirect === 'string' && redirect.startsWith('/') && !redirect.startsWith('//')
    ? redirect
    : '/'
}

function renderGoogleButton() {
  if (!button.value || !window.google || !googleClientId) return
  window.google.accounts.id.initialize({
    client_id: googleClientId,
    callback: async ({ credential }) => {
      isLoading.value = true
      error.value = ''
      try {
        await authStore.signInWithGoogle(credential)
        await router.replace(getRedirectPath())
      } catch (cause) {
        error.value = cause instanceof Error ? cause.message : '로그인에 실패했습니다.'
      } finally {
        isLoading.value = false
      }
    },
  })
  window.google.accounts.id.renderButton(button.value, {
    theme: 'outline',
    size: 'large',
    shape: 'rectangular',
    text: 'signin_with',
    width: 280,
  })
}

onMounted(() => {
  if (!googleClientId) {
    error.value = '현재 Google 로그인을 이용할 수 없습니다. 잠시 후 다시 시도해 주세요.'
    return
  }
  script = document.createElement('script')
  script.src = 'https://accounts.google.com/gsi/client'
  script.async = true
  script.defer = true
  script.onload = renderGoogleButton
  script.onerror = () => {
    error.value = 'Google 로그인 서비스를 불러오지 못했습니다.'
  }
  document.head.appendChild(script)
})

onUnmounted(() => {
  script?.remove()
})
</script>

<template>
  <main class="flex min-h-screen items-center justify-center bg-(--color-background) px-6">
    <section class="w-full max-w-md rounded-2xl border border-(--color-border) bg-(--color-surface) p-8 text-center shadow-sm">
      <h1 class="text-2xl font-bold text-(--color-heading)">로그인</h1>
      <p class="mt-3 text-sm text-(--color-text-secondary)">다음 계정으로 로그인하세요.</p>
      <div class="mt-8 flex justify-center" :aria-busy="isLoading">
        <div ref="button" />
      </div>
      <p v-if="isLoading" class="mt-4 text-sm text-(--color-text-secondary)">로그인 중...</p>
      <p v-if="error" role="alert" class="mt-4 text-sm text-red-600">{{ error }}</p>
      <RouterLink to="/" class="mt-8 inline-block text-sm text-(--color-primary) hover:underline">
        블로그로 돌아가기
      </RouterLink>
    </section>
  </main>
</template>
