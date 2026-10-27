<script setup lang="ts">
import { LogOut } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 로그인한 모든 사용자에게 보이는 메뉴. 항목을 추가하려면 template에 버튼을 늘리면 됩니다.
const authStore = useAuthStore()
const router = useRouter()

function logout() {
  authStore.logout()
  void router.push('/')
}
</script>

<template>
  <button
    type="button"
    title="Logout"
    aria-label="Logout"
    class="flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:cursor-pointer hover:border-(--color-primary) hover:text-(--color-primary)"
    @click="logout"
  >
    <LogOut class="h-4 w-4" />
  </button>
</template>
