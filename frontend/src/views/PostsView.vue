<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { Book } from 'lucide-vue-next'
import { posts, type Post } from '@/data/posts'
import PostCard from '@/components/blog/PostCard.vue'

const postsByYear = Object.entries(
  posts.reduce<Record<string, Post[]>>((groups, post) => {
    const year = post.date.split(', ').pop()!
    ;(groups[year] ??= []).push(post)
    return groups
  }, {}),
).sort(([a], [b]) => Number(b) - Number(a))
</script>

<template>
  <BlogLayout>
    <header class="mb-10">
      <h1 class="flex items-center text-center gap-3 text-4xl font-bold text-(--color-heading)">
        <Book class="h-9 w-9" />
        POSTS
      </h1>
      <p class="mt-2 text-sm text-(--color-text-secondary)">
        &nbsp;배우고, 만들고, 고민한 것들을 기록합니다.
      </p>
    </header>

    <section v-for="([year, yearPosts], index) in postsByYear" :key="year" class="mb-10 w-full">
      <div class="mb-2 flex items-center gap-3">
        <h2 class="text-2xl font-black text-(--color-heading) text-center">{{ year }}</h2>
        <span
          class="rounded-full border border-(--color-border) px-2.5 py-0.5 translate-y-0.5 text-xs text-center font-medium text-(--color-text-secondary)"
        >
          {{ yearPosts.length }} posts
        </span>
      </div>

      <ul class="flex flex-col">
        <li v-for="post in yearPosts" :key="post.title" class="flex items-start">
          <PostCard
            :title="post.title"
            :description="post.description"
            :date="post.date"
            :tags="post.tags"
          />
        </li>
      </ul>
      <hr v-if="index < postsByYear.length - 1" class="mt-8 border-(--color-border)" />
    </section>
  </BlogLayout>
</template>
