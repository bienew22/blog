<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { ref, onMounted, computed } from 'vue'
import { fetchPostDetail, type PostDetail } from '@/api/posts'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const slug = computed(() => route.params.slug as string)

const post = ref<PostDetail | null>(null)
const isLoading = ref(true)
const error = ref<string | null>(null)

onMounted(async () => {
  try {
    post.value = await fetchPostDetail(slug.value)
    if (post.value === null) {
      router.replace({ name: 'not-found' })
    }
  } catch (e) {
    error.value = e instanceof Error ? e.message : '포스트를 불러오지 못했습니다.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <BlogLayout>
    <!-- <BlogHeader :icon="TagIcon" :title="post?.title" :description="post?.content" /> -->

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="text-sm text-red-500">{{ error }}</p>
    <div v-else class="prose max-w-none">
      <h1>{{ post?.title }}</h1>
      <div v-html="post?.content"></div>
    </div>
  </BlogLayout>
</template>
