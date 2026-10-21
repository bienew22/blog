<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import { House, Menu, Book, X, Tags } from 'lucide-vue-next'
import { CategoryIcon } from '@/components/icon'
import ThemeToggle from '@/components/blog-sidebar/ThemeToggle.vue'
import GithubButton from '@/components/blog-sidebar/GithubButton.vue'

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
    class="sticky top-0 z-30 flex min-h-16 items-center justify-between border-b border-(--color-border) bg-(--color-surface) px-4 py-3 lg:hidden"
  >
    <RouterLink to="/" class="flex items-center gap-2" @click="isOpen = false">
      <span class="h-8 w-8 overflow-hidden rounded-full border border-(--color-border)">
        <img src="/avatar.png" alt="bienew" class="h-full w-full object-cover" />
      </span>
      <span class="text-sm font-bold text-(--color-heading)">bienew</span>
    </RouterLink>
    <button
      type="button"
      class="flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary) hover:cursor-pointer"
      :aria-expanded="isOpen"
      aria-label="메뉴 열기"
      @click="isOpen = !isOpen"
    >
      <Menu v-if="!isOpen" class="h-5 w-5" />
      <X v-else class="h-5 w-5" />
    </button>
  </header>

  <div v-if="isOpen" class="fixed inset-0 z-20 bg-black/40 lg:hidden" @click="isOpen = false" />

  <aside
    class="fixed inset-y-0 left-0 z-30 flex h-screen w-72 -translate-x-full shrink-0 flex-col overflow-y-auto border-r border-(--color-border) bg-(--color-surface) px-6 py-8 transition-transform duration-300 ease-in-out lg:sticky lg:top-0 lg:w-(--sidebar-width) lg:translate-x-0"
    :class="isOpen ? 'translate-x-0' : '-translate-x-full'"
  >
    <RouterLink
      to="/"
      class="group flex flex-col items-center border-b border-(--color-border-subtle) pb-6 text-center"
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

    <nav aria-label="주요 메뉴" class="flex flex-1 flex-col justify-center gap-2 py-8">
      <RouterLink
        v-for="item in navItems"
        :key="item.label"
        :to="item.to"
        class="flex min-h-11 items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-(--color-text-secondary) transition-colors hover:bg-(--color-background-soft) hover:text-(--color-primary)"
        active-class="bg-(--color-accent-soft)! font-semibold! text-(--color-primary)!"
        @click="isOpen = false"
      >
        <component :is="item.icon" class="h-4 w-4" />
        {{ item.label }}
      </RouterLink>
    </nav>

    <div class="flex items-center justify-center gap-3 border-t border-(--color-border-subtle) pt-5">
      <ThemeToggle />
      <span class="text-2xl text-(--color-border-hover)">·</span>

      <GithubButton />
      <!-- <span class="text-3xl text-(--color-border-hover)">·</span> -->
    </div>
  </aside>
</template>
