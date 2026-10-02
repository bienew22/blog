<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import BlogHeader from '@/components/layout/BlogHeader.vue'
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
    <BlogHeader
      :icon="Tags"
      title="TAGS"
      description="개발하며 배우고 기록한 모든 주제를 모아두었습니다."
    />

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
