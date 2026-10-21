<script setup lang="ts">
import type { Component } from 'vue'
import { NotebookText } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'

withDefaults(
  defineProps<{
    title: string
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
  <div
    class="post-card group flex w-full items-start justify-between gap-4 rounded-lg border-b border-(--color-border-subtle) px-3 py-5 transition-colors hover:bg-(--color-surface)"
  >
    <RouterLink :to="`/post/${href}`" class="flex min-w-0 flex-1 items-start gap-3">
      <span class="mt-0.5 flex h-8 w-8 shrink-0 items-center justify-center rounded-md bg-(--color-background-soft) text-(--color-primary)">
        <component :is="icon" class="h-4 w-4" />
      </span>

      <div class="min-w-0 max-w-200">
        <span
          class="break-words text-base font-semibold leading-snug text-(--color-heading) underline-offset-4 transition-colors group-hover:text-(--color-primary) group-hover:underline"
        >
          {{ title }}
        </span>

        <p class="mt-2 text-xs text-(--color-text-muted)">{{ date }}</p>
      </div>
    </RouterLink>

    <ul class="hidden max-w-64 shrink-0 flex-wrap justify-end gap-2 sm:flex">
      <li v-for="tag in tags" :key="tag">
        <RouterLink
          :to="`/tag/${tag}`"
          class="max-w-full break-all rounded-full border border-(--color-border) bg-(--color-background) px-3 py-1 text-xs font-medium text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:bg-(--color-accent-soft) hover:text-(--color-primary)"
        >
          #{{ tag }}
        </RouterLink>
      </li>
    </ul>
  </div>
</template>
