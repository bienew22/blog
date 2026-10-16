<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { RouterLink } from 'vue-router'
</script>

<template>
  <BlogLayout>
    <div class="flex min-h-[60vh] flex-col items-center justify-center text-center">
      <p class="text-sm font-semibold text-(--color-text-secondary)">404</p>
      <h1 class="mt-2 text-3xl font-bold text-(--color-heading)">페이지를 찾을 수 없습니다.</h1>
      <p class="mt-3 text-sm text-(--color-text-secondary)">
        요청하신 페이지가 존재하지 않거나 이동되었습니다.
      </p>
      <RouterLink
        to="/"
        class="mt-6 rounded-full border border-(--color-border) px-5 py-2 text-sm font-medium text-(--color-heading) hover:border-(--color-border-hover)"
      >
        홈으로 돌아가기
      </RouterLink>
    </div>
  </BlogLayout>
</template>
