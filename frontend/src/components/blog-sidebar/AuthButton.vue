<script setup lang="ts">
import { LogIn, UserRound } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AdminMenuItems from './AdminMenuItems.vue'
import Tooltip from './Tooltip.vue'
import UserMenuItems from './UserMenuItems.vue'

const authStore = useAuthStore()
</script>

<template>
  <RouterLink
    v-if="!authStore.user"
    to="/login"
    class="group relative flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary) hover:cursor-pointer"
  >
    <LogIn class="h-4 w-4" />
    <Tooltip content="Login" />
  </RouterLink>
  <div v-else class="group/menu relative">
    <button
      type="button"
      aria-label="Account menu"
      class="flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors group-hover/menu:border-(--color-primary) group-hover/menu:text-(--color-primary) hover:cursor-pointer"
    >
      <UserRound class="h-4 w-4" />
    </button>

    <div
      class="invisible absolute bottom-full left-1/2 z-10 -translate-x-1/2 pb-2 opacity-0 transition group-focus-within/menu:visible group-focus-within/menu:opacity-100 group-hover/menu:visible group-hover/menu:opacity-100"
    >
      <div class="flex flex-col gap-2">
        <AdminMenuItems v-if="authStore.isAdmin" />
        <UserMenuItems />
      </div>
    </div>
  </div>
</template>
