<script setup lang="ts">
import { ArrowRight } from 'lucide-vue-next'

defineProps<{
  index: number
  categoryName: string
  categoryDesc?: string
  postCount: number
  lastPostCreateAt?: string | null
}>()

function formatDate(createAt?: string | null) {
  if (!createAt) return null

  const [date = ''] = createAt.replace(' ', 'T').split('T')
  const [year, month, day] = date.split('-')
  if (!year || !month || !day) return null

  return `${year}.${month}.${day}`
}
</script>

<template>
  <div
    class="category-card h-full min-h-24 rounded-2xl bg-(--color-surface) px-5 py-5 lg:min-h-[220px] lg:px-6 lg:py-6"
  >
    <div class="flex h-full items-center gap-4 lg:hidden">
      <span
        class="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-(--color-category-index-bg) text-base font-bold text-(--color-primary)"
      >
        {{ String(index).padStart(2, '0') }}
      </span>
      <span class="min-w-0 flex-1">
        <span class="block break-words text-lg font-bold text-(--color-heading)">{{ categoryName }}</span>
        <span v-if="categoryDesc" class="mt-1 block text-sm text-(--color-text-secondary)">{{
          categoryDesc
        }}</span>
      </span>
      <span class="shrink-0 text-right">
        <span class="block text-2xl font-bold leading-none text-(--color-primary)">{{ postCount }}</span>
        <span class="mt-1 block text-xs text-(--color-text-muted)">개의 글</span>
      </span>
    </div>
    <div class="hidden h-full flex-col lg:flex">
      <div class="flex items-center justify-between">
        <span
          class="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-(--color-category-index-bg) text-base font-bold text-(--color-primary)"
        >
          {{ String(index).padStart(2, '0') }}
        </span>
        <span class="rounded-full bg-(--color-background-soft) px-3 py-1 text-sm text-(--color-text-secondary)">
          {{ postCount }}개의 글
        </span>
      </div>
      <div class="mt-7">
        <h2 class="break-words text-xl font-bold text-(--color-heading)">{{ categoryName }}</h2>
        <p v-if="categoryDesc" class="mt-1 text-sm text-(--color-text-secondary)">{{ categoryDesc }}</p>
      </div>
      <div class="mt-auto flex items-center justify-between border-t border-(--color-border-subtle) pt-4 text-sm text-(--color-text-secondary)">
        <span>최근 글 · {{ formatDate(lastPostCreateAt) ?? '없음' }}</span>
        <ArrowRight class="h-5 w-5" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.category-card {
  border: 1px solid var(--color-border-subtle);
  transition:
    border-color 0.3s ease,
    box-shadow 0.3s ease,
    transform 0.3s ease;
}

.category-card:hover {
  border-color: var(--color-primary);
  cursor: pointer;
  box-shadow: 0 4px 16px var(--color-card-shadow);
  transform: translateY(-2px);
}

@media (prefers-reduced-motion: reduce) {
  .category-card {
    transition: none;
  }

  .category-card:hover {
    transform: none;
  }
}
</style>
