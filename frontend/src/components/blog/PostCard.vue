<script setup lang="ts">
defineProps<{
  title: string
  description: string
  date: string
  tags: string[]
}>()
</script>

<template>
  <a
    href="#"
    class="block rounded-xl border border-(--color-border) bg-(--color-background-soft) p-6 transition hover:border-(--color-border-hover)"
  >
    <h2 class="text-lg font-bold text-(--color-heading)">{{ title }}</h2>
    <p class="mt-2 line-clamp-2 text-sm leading-relaxed text-(--color-text-secondary)">
      {{ description }}
    </p>
    <div class="mt-4 flex flex-wrap items-center gap-4 text-xs text-(--color-text-secondary)">
      <span class="flex items-center gap-1.5">
        <svg
          class="h-3.5 w-3.5"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <rect x="3" y="4" width="18" height="18" rx="2" />
          <path stroke-linecap="round" d="M16 2v4M8 2v4M3 10h18" />
        </svg>
        {{ date }}
      </span>
      <span class="flex items-center gap-1.5">
        <svg
          class="h-3.5 w-3.5"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M3 7a2 2 0 012-2h4l2 2h8a2 2 0 012 2v8a2 2 0 01-2 2H5a2 2 0 01-2-2V7z"
          />
        </svg>
        {{ tags.join(', ') }}
      </span>
    </div>
  </a>
</template>
