<script setup lang="ts">
import { RouterLink } from 'vue-router'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import PostCard from '@/components/blog/PostCard.vue'
import { posts } from '@/data/posts'

const latestPosts = posts.slice(0, 4)
</script>

<template>
  <BlogLayout>
    <section class="flex flex-col gap-5">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-bold text-(--color-heading)">Latest</h2>
        <RouterLink
          to="/posts"
          class="text-sm font-medium text-(--color-text-secondary) transition hover:text-(--color-heading)"
        >
          All Posts
        </RouterLink>
      </div>
      <PostCard v-for="post in latestPosts" :key="post.title" v-bind="post" />
    </section>
  </BlogLayout>
</template>
