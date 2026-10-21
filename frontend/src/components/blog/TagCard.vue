<script setup lang="ts">
import { Tag, ChevronRight } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'

withDefaults(
  defineProps<{
    tagName: string
    postCount: number
  }>(),
  {},
)
</script>

<template>
  <RouterLink
    :to="`/tag/${tagName}`"
    class="tag-card group flex min-h-20 items-center justify-between gap-3 rounded-lg border border-(--color-border) bg-(--color-surface) px-5 py-4 transition-colors hover:border-(--color-primary) hover:bg-(--color-accent-soft)"
  >
    <span class="flex min-w-0 items-center gap-3">
      <span
        class="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-(--color-background-soft) text-(--color-primary)"
      >
        <Tag class="h-5 w-5" />
      </span>
      <span
        class="break-all font-semibold text-(--color-heading) group-hover:text-(--color-primary)"
        >{{ tagName }}</span
      >
    </span>
    <span
      class="flex shrink-0 items-center gap-1 text-sm whitespace-nowrap text-(--color-text-secondary) group-hover:text-(--color-primary)"
    >
      {{ postCount }}
      <ChevronRight class="h-4 w-4" />
    </span>
  </RouterLink>
</template>
