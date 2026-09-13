<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import { useThemeStore } from '@/stores/theme'
import { House, Menu, Book, X, Tags } from 'lucide-vue-next'
import { CategoryIcon, LightThemeIcon, DarkThemeIcon, GithubIcon } from '@/components/icon'

const theme = useThemeStore()
const isOpen = ref(false)

const navItems = [
  { label: 'HOME', to: '/', icon: House },
  { label: 'POSTS', to: '/posts', icon: Book },
  { label: 'CATEGORIES', to: '/categories', icon: CategoryIcon },
  { label: 'TAGS', to: '/tags', icon: Tags },
]
</script>

<template>
  <header
    class="sticky top-0 z-30 flex items-center justify-between border-b border-(--color-border) bg-(--color-background) px-4 py-3 lg:hidden"
  >
    <RouterLink to="/" class="flex items-center gap-2" @click="isOpen = false">
      <span class="h-8 w-8 overflow-hidden rounded-full border border-(--color-border)">
        <img src="/avatar.png" alt="bienew" class="h-full w-full object-cover" />
      </span>
      <span class="text-sm font-bold text-(--color-heading)">bienew</span>
    </RouterLink>
    <button
      type="button"
      class="flex h-9 w-9 items-center justify-center rounded-full bg-(--color-background-mute) text-(--color-text-secondary) hover:cursor-pointer"
      aria-label="Toggle menu"
      @click="isOpen = !isOpen"
    >
      <Menu v-if="!isOpen" class="h-5 w-5" />
      <X v-else class="h-5 w-5" />
    </button>
  </header>

  <div v-if="isOpen" class="fixed inset-0 z-20 bg-black/40 lg:hidden" @click="isOpen = false" />

  <aside
    class="fixed inset-y-0 left-0 z-30 flex h-screen w-72 -translate-x-full shrink-0 flex-col overflow-y-auto border-r border-(--color-border) bg-(--color-background) px-6 py-8 transition-transform duration-300 ease-in-out lg:sticky lg:top-0 lg:w-auto lg:min-w-(--sidebar-width) lg:translate-x-0"
    :class="isOpen ? 'translate-x-0' : '-translate-x-full'"
  >
    <RouterLink
      to="/"
      class="group flex flex-col items-center text-center border-b border-(--color-border) pb-4"
      @click="isOpen = false"
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

    <nav class="flex flex-1 flex-col justify-center -translate-y-15 gap-1">
      <RouterLink
        v-for="item in navItems"
        :key="item.label"
        :to="item.to"
        class="flex items-center gap-3 rounded-lg px-3 py-2.5 text-[14px] font-bold text-(--color-text-secondary) transition-colors duration-500 ease-in-out hover:bg-(--color-background-mute) hover:text-(--color-heading)"
        active-class="bg-(--color-background-mute)! text-(--color-heading)! transition-colors! duration-500! ease-in-out!"
        @click="isOpen = false"
      >
        <component :is="item.icon" class="h-4 w-4" />
        {{ item.label }}
      </RouterLink>
    </nav>

    <div class="flex items-center justify-center gap-2 border-t border-(--color-border) pt-4">
      <button
        type="button"
        class="group relative flex h-7 w-7 items-center justify-center rounded-full bg-(--color-background-mute) text-(--color-text-secondary) transition duration-500 hover:text-(--color-heading) hover:cursor-pointer"
        aria-label="Toggle theme"
        @click="theme.toggleTheme"
      >
        <component :is="theme.isDark ? DarkThemeIcon : LightThemeIcon" class="h-4 w-4" />

        <span
          class="pointer-events-none absolute -top-7 left-1/2 -translate-x-1/2 rounded bg-(--color-heading) px-1.5 py-0.5 text-xs whitespace-nowrap text-(--color-background) opacity-0 transition duration-500 after:absolute after:top-full after:left-1/2 after:-translate-x-1/2 after:border-4 after:border-transparent after:border-t-(--color-heading) after:content-[''] group-hover:opacity-100"
        >
          Theme
        </span>
      </button>
      <span class="text-2xl text-(--color-border-hover)">·</span>
      <a
        href="https://github.com"
        target="_blank"
        rel="noopener noreferrer"
        class="group relative flex h-7 w-7 items-center justify-center rounded-full bg-(--color-background-mute) text-(--color-text-secondary) transition duration-500 hover:text-(--color-heading)"
        aria-label="GitHub"
      >
        <GithubIcon class="h-4 w-4" />
        <span
          class="pointer-events-none absolute -top-7 left-1/2 -translate-x-1/2 rounded bg-(--color-heading) px-1.5 py-0.5 text-xs whitespace-nowrap text-(--color-background) opacity-0 transition duration-500 after:absolute after:top-full after:left-1/2 after:-translate-x-1/2 after:border-4 after:border-transparent after:border-t-(--color-heading) after:content-[''] group-hover:opacity-100"
        >
          GitHub
        </span>
      </a>
      <!-- <span class="text-3xl text-(--color-border-hover)">·</span> -->
    </div>
  </aside>
</template>
