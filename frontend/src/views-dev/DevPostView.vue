<script setup lang="ts">
import type { PostDetail } from '@/api/posts'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { dummyPost } from '@/data/post'
import PostView from '@/views/PostView.vue'

const mockPost: PostDetail = {
  ...dummyPost,
  id: null,
  contentMd: '',
  category: { categoryId: 1, categoryName: '알고리즘' },
}
</script>

<template>
  <BlogLayout>
    <PostView :preview="mockPost" />
  </BlogLayout>
</template>
