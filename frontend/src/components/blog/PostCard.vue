<script setup lang="ts">
import type { Component } from 'vue'
import { NotebookText } from 'lucide-vue-next'

withDefaults(
  defineProps<{
    title: string
    description: string
    date: string
    tags: string[]
    icon?: Component
    href?: string
  }>(),
  {
    icon: () => NotebookText,
    href: '#',
  },
)
</script>

<template>
  <a
    :href="href"
    class="post-card group flex w-full items-start gap-10 rounded-xl px-1 py-2 transition-colors duration-300 hover:bg-(--color-background-mute)"
  >
    <div class="flex min-w-0 flex-1 items-start gap-1">
      <span class="flex h-6.5 w-7 shrink-0 items-center justify-center text-(--color-heading)">
        <component :is="icon" class="h-4 w-4" />
      </span>

      <div class="min-w-0 max-w-200">
        <span
          class="text-[0.9rem] font-bold text-(--color-heading) underline-offset-3 transition-colors duration-300 group-hover:text-(--color-hover-title) group-hover:underline group-hover:decoration-(--color-hover-title)"
        >
          {{ title }}
        </span>

        <p class="mt-2 text-xs text-(--color-text-secondary)">{{ date }}</p>
      </div>
    </div>

    <ul class="hidden max-w-64 pr-3 flex-wrap justify-end gap-1 overflow-hidden md:flex">
      <li
        v-for="tag in tags"
        :key="tag"
        class="rounded-lg border border-(--color-border) px-2.5 py-1 text-xs font-medium text-(--color-text-secondary)"
      >
        #{{ tag }}
      </li>
    </ul>
  </a>
</template>
