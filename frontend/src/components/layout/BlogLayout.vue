<script setup lang="ts">
import BlogSidebar from './BlogSidebar.vue'
</script>

<template>
  <div class="flex min-h-screen bg-(--color-background) text-(--color-text)">
    <BlogSidebar />
    <main class="flex min-h-screen flex-1 flex-col">
      <div class="mx-auto w-full max-w-3xl flex-1 px-8 py-10">
        <slot />
      </div>
    </main>
  </div>
</template>
