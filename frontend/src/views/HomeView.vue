<script setup lang="ts">
import { ArrowRight, BookOpen, Tags } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
import BlogLayout from '@/components/layout/BlogLayout.vue'
</script>

<template>
  <BlogLayout>
    <section
      class="mx-auto flex min-h-[58vh] w-full max-w-3xl flex-col justify-center py-12 sm:py-20"
    >
      <p class="mb-4 text-sm font-semibold tracking-wide text-(--color-primary)">DEVLOG</p>
      <h1 class="max-w-2xl text-4xl leading-tight font-bold tracking-tight text-(--color-heading) sm:text-5xl">
        배우고 만든 것을<br class="hidden sm:block" /> 차곡차곡 기록합니다.
      </h1>
      <p class="mt-6 max-w-xl text-base leading-relaxed text-(--color-text-secondary) sm:text-lg">
        개발하며 마주친 문제와 해결 과정, 새롭게 배운 기술을 나중에 다시 찾기 쉽도록 정리하는 공간입니다.
      </p>
      <div class="mt-8 flex flex-wrap gap-3">
        <RouterLink
          to="/posts"
          class="inline-flex min-h-11 items-center gap-2 rounded-lg bg-(--color-primary) px-5 py-2.5 text-sm font-semibold text-white transition-colors hover:bg-(--color-primary-hover)"
        >
          <BookOpen class="h-4 w-4" />
          글 살펴보기
          <ArrowRight class="h-4 w-4" />
        </RouterLink>
        <RouterLink
          to="/tags"
          class="inline-flex min-h-11 items-center gap-2 rounded-lg border border-(--color-border) bg-(--color-surface) px-5 py-2.5 text-sm font-semibold text-(--color-text) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
        >
          <Tags class="h-4 w-4" />
          태그로 찾기
        </RouterLink>
      </div>
    </section>
  </BlogLayout>
</template>
