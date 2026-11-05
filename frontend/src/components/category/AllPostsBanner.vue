<script setup lang="ts">
import { ArrowRight } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'

defineProps<{
  categoryCount: number
  totalPostCount: number
}>()
</script>

<template>
  <RouterLink
    :to="{ name: 'posts' }"
    class="category-banner mb-4 flex items-center justify-between gap-4 rounded-2xl px-6 py-5 hover:cursor-pointer"
  >
    <span>
      <span class="block text-lg font-bold">전체 글 보기</span>
      <span class="mt-1 block text-sm text-(--color-category-banner-muted)">
        {{ categoryCount }}개 카테고리, 총 {{ totalPostCount }}개의 글
      </span>
    </span>
    <ArrowRight class="h-6 w-6 shrink-0" />
  </RouterLink>
</template>

<style scoped>
.category-banner {
  background-color: var(--color-category-banner);
  color: var(--color-category-banner-text);
}

.category-banner:hover {
  background-color: var(--color-category-banner-hover);
}
</style>
