<script setup lang="ts">
import type { Component } from 'vue'

defineProps<{
  title?: string
  icon?: Component
  description?: string
}>()
</script>

<template>
  <header class="mb-8">
    <h1 class="flex items-center gap-3 text-2xl font-bold tracking-tight text-(--color-heading) sm:text-3xl">
      <component v-if="icon" :is="icon" class="h-7 w-7 shrink-0 text-(--color-primary) sm:h-8 sm:w-8" />
      {{ title }}
    </h1>
    <p v-if="description" class="mt-2 text-sm leading-relaxed text-(--color-text-secondary) sm:text-base">
      &nbsp;{{ description }}
    </p>
  </header>
</template>
