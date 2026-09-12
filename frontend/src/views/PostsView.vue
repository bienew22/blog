<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import PostCard from '@/components/blog/PostCard.vue'
import { posts } from '@/data/posts'
</script>

<template>
  <BlogLayout>
    <h1 class="mb-8 text-2xl font-bold text-(--color-heading)">Posts</h1>
    <div class="flex flex-col gap-5">
      <PostCard v-for="post in posts" :key="post.title" v-bind="post" />
    </div>
  </BlogLayout>
</template>
