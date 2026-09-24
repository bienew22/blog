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
    class="tag-card group flex items-center justify-between gap-3 rounded-2xl border border-(--color-border) bg-(--color-background-soft) px-5 py-4 transition-all duration-200 ease-out hover:bg-(--color-background-mute) hover:border-(--color-border-hover)"
  >
    <span class="flex min-w-0 items-center gap-3">
      <span
        class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl border border-(--color-border) bg-(--color-background-mute)"
      >
        <Tag class="h-5 w-5" />
      </span>
      <span
        class="truncate font-semibold text-(--color-heading) group-hover:text-(--color-hover-title)"
        >{{ tagName }}</span
      >
    </span>
    <span
      class="flex shrink-0 items-center gap-1 text-sm whitespace-nowrap text-(--color-text-secondary) group-hover:text-(--color-hover-title)"
    >
      {{ postCount }}
      <ChevronRight class="h-4 w-4" />
    </span>
  </RouterLink>
</template>
