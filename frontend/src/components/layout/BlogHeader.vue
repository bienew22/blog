<script setup lang="ts">
import type { Component } from 'vue'

defineProps<{
  title?: string
  icon?: Component
  description?: string
}>()
</script>

<template>
  <header class="mb-10">
    <h1 class="flex items-center text-center gap-3 text-4xl font-bold text-(--color-heading)">
      <component v-if="icon" :is="icon" class="h-9 w-9" />
      {{ title }}
    </h1>
    <p class="mt-2 text-sm text-(--color-text-secondary)">&nbsp;&nbsp;{{ description }}</p>
  </header>
</template>
