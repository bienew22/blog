<script setup lang="ts">
import { RouterLink } from 'vue-router'

defineProps<{
  title: string
  date: string
  slug: string
}>()
</script>

<template>
  <RouterLink :to="`/post/${slug}`" class="group flex items-center gap-2 py-1.5 text-sm">
    <span class="h-1.5 w-1.5 shrink-0 rounded-full bg-(--color-text-secondary)"></span>
    <span
      :title="title"
      class="min-w-0 max-w-[65%] truncate font-medium text-blue-600 group-hover:underline dark:text-blue-400"
    >
      {{ title }}
    </span>
    <!-- dotted fill between title and date -->
    <span
      class="mx-1 h-0 flex-1 self-center border-b-[3px] border-dotted border-(--color-border-hover)"
    ></span>
    <span class="shrink-0 text-(--color-text-secondary)">{{ date }}</span>
  </RouterLink>
</template>
