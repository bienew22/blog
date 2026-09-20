<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { ref, onMounted } from 'vue'
import { Tags } from 'lucide-vue-next'
import { fetchTags, type Tag } from '@/api/tags'
import TagCard from '@/components/blog/TagCard.vue'

const tags = ref<Tag[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)

onMounted(async () => {
  try {
    tags.value = await fetchTags()
  } catch (e) {
    error.value = e instanceof Error ? e.message : '태그를 불러오지 못했습니다.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <BlogLayout>
    <header class="mb-10">
      <h1 class="flex items-center text-center gap-3 text-4xl font-bold text-(--color-heading)">
        <Tags class="h-9 w-9" />
        TAGS
      </h1>
      <p class="mt-2 text-sm text-(--color-text-secondary)">
        &nbsp;개발하며 배우고 기록한 모든 주제를 모아두었습니다.
      </p>
    </header>

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="text-sm text-red-500">{{ error }}</p>

    <ul v-else class="grid grid-cols-1 gap-3 md:grid-cols-2">
      <li v-for="tag in tags" :key="tag.tagName">
        <TagCard :tagName="tag.tagName" :postCount="tag.postCount" />
      </li>
    </ul>
  </BlogLayout>
</template>
