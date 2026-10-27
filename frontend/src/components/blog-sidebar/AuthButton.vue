<script setup lang="ts">
import { LogIn, LogOut } from 'lucide-vue-next'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Tooltip from './Tooltip.vue'

const authStore = useAuthStore()
const router = useRouter()

function logout() {
  authStore.logout()
  if (router.currentRoute.value.meta.requiresAdmin) {
    void router.replace('/')
  }
}
</script>

<template>
  <RouterLink
    v-if="!authStore.user"
    to="/login"
    class="group relative flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
  >
    <LogIn class="h-4 w-4" />
    <Tooltip content="Login" />
  </RouterLink>
  <button
    v-else
    type="button"
    class="group relative flex h-11 w-11 items-center justify-center overflow-hidden rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
    aria-label="logout"
    @click="logout"
  >
    <LogOut class="h-4 w-4" />
    <Tooltip content="Logout" />
  </button>
</template>
