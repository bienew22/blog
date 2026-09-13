<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { useThemeStore } from '@/stores/theme'
import { House } from 'lucide-vue-next'
import { CategoryIcon, PostIcon } from '@/components/icon'

const theme = useThemeStore()

const navItems = [
  { label: 'HOME', to: '/', icon: House },
  { label: 'POSTS', to: '/posts', icon: PostIcon },
  { label: 'CATEGORIES', to: '/categories', icon: CategoryIcon },
]
</script>

<template>
  <aside
    class="sticky top-0 flex h-screen min-w-(--sidebar-width) shrink-0 flex-col border-r border-(--color-border) bg-(--color-background) px-6 py-8"
  >
    <RouterLink
      to="/"
      class="group flex flex-col items-center text-center border-b border-(--color-border) pb-4"
    >
      <span class="h-20 w-20 overflow-hidden rounded-full border border-(--color-border)">
        <img
          src="/avatar.png"
          alt="bienew"
          class="h-full w-full object-cover transition-transform duration-500 ease-in-out group-hover:scale-110"
        />
      </span>
      <h1 class="mt-4 text-lg font-bold text-(--color-heading)">bienew</h1>
      <p class="mt-1 text-xs text-(--color-text-secondary)">Never Stop Becoming Better</p>
    </RouterLink>

    <nav class="flex flex-1 flex-col justify-center -translate-y-20 gap-1">
      <RouterLink
        v-for="item in navItems"
        :key="item.label"
        :to="item.to"
        class="flex items-center gap-3 rounded-lg px-3 py-2.5 text-[14px] font-bold text-(--color-text-secondary) transition-colors duration-500 ease-in-out hover:bg-(--color-background-mute) hover:text-(--color-heading)"
        active-class="bg-(--color-background-mute)! text-(--color-heading)! transition-colors! duration-500! ease-in-out!"
      >
        <component :is="item.icon" class="h-4 w-4" />
        {{ item.label }}
      </RouterLink>
    </nav>

    <div class="flex items-center justify-center gap-2 border-t border-(--color-border) pt-6">
      <button
        type="button"
        class="group relative flex h-9 w-9 items-center justify-center rounded-full bg-(--color-background-mute) text-(--color-text-secondary) transition duration-500 hover:text-(--color-heading) hover:cursor-pointer"
        aria-label="Toggle theme"
        @click="theme.toggleTheme"
      >
        <svg
          v-if="!theme.isDark"
          class="h-5 w-5"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"
          />
        </svg>
        <svg v-else class="h-5 w-5" viewBox="0 0 24 24" fill="currentColor">
          <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" />
        </svg>
        <span
          class="pointer-events-none absolute -top-8 left-1/2 -translate-x-1/2 rounded bg-(--color-heading) px-2 py-1 text-xs whitespace-nowrap text-(--color-background) opacity-0 transition duration-500 after:absolute after:top-full after:left-1/2 after:-translate-x-1/2 after:border-4 after:border-transparent after:border-t-(--color-heading) after:content-[''] group-hover:opacity-100"
        >
          Theme
        </span>
      </button>
      <span class="text-3xl text-(--color-border-hover)">·</span>
      <a
        href="https://github.com"
        target="_blank"
        rel="noopener noreferrer"
        class="group relative flex h-9 w-9 items-center justify-center rounded-full bg-(--color-background-mute) text-(--color-text-secondary) transition duration-500 hover:text-(--color-heading)"
        aria-label="GitHub"
      >
        <svg class="h-5 w-5" viewBox="0 0 24 24" fill="currentColor">
          <path
            d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.833.091-.647.35-1.088.636-1.339-2.221-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.269 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.295 2.747-1.026 2.747-1.026.546 1.378.202 2.397.1 2.65.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.31.678.921.678 1.856 0 1.34-.012 2.421-.012 2.751 0 .268.18.58.688.482A10.02 10.02 0 0022 12.017C22 6.484 17.522 2 12 2z"
          />
        </svg>
        <span
          class="pointer-events-none absolute -top-8 left-1/2 -translate-x-1/2 rounded bg-(--color-heading) px-2 py-1 text-xs whitespace-nowrap text-(--color-background) opacity-0 transition duration-500 after:absolute after:top-full after:left-1/2 after:-translate-x-1/2 after:border-4 after:border-transparent after:border-t-(--color-heading) after:content-[''] group-hover:opacity-100"
        >
          GitHub
        </span>
      </a>
      <span class="text-3xl text-(--color-border-hover)">·</span>
      <a
        href="/rss.xml"
        class="group relative flex h-9 w-9 items-center justify-center rounded-full bg-(--color-background-mute) text-(--color-text-secondary) transition duration-500 hover:text-(--color-heading)"
        aria-label="RSS"
      >
        <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M4 11a9 9 0 019 9M4 4a16 16 0 0116 16"
          />
          <circle cx="5" cy="19" r="1.5" fill="currentColor" stroke="none" />
        </svg>
        <span
          class="pointer-events-none absolute -top-8 left-1/2 -translate-x-1/2 rounded bg-(--color-heading) px-2 py-1 text-xs whitespace-nowrap text-(--color-background) opacity-0 transition duration-500 after:absolute after:top-full after:left-1/2 after:-translate-x-1/2 after:border-4 after:border-transparent after:border-t-(--color-heading) after:content-[''] group-hover:opacity-100"
        >
          RSS
        </span>
      </a>
    </div>
  </aside>
</template>
