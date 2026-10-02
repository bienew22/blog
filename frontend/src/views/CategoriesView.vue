<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'

defineProps<{
  title: string
}>()
</script>

<template>
  <BlogLayout>
    <h1 class="mb-4 text-2xl font-bold text-(--color-heading)">{{ title }}</h1>
    <div
      class="flex flex-1 items-center justify-center py-16 text-sm text-(--color-text-secondary)"
    >
      준비 중입니다.
    </div>
  </BlogLayout>
</template>
