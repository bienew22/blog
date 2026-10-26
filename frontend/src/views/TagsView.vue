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
    <p v-else-if="error" class="rounded-lg border border-(--color-error) bg-(--color-surface) p-4 text-sm text-(--color-error)">
      태그를 불러오지 못했습니다. 잠시 후 다시 시도해주세요. {{ error }}
    </p>

    <div
      v-else-if="tags.length === 0"
      class="rounded-lg border border-(--color-border) bg-(--color-surface) px-6 py-12 text-center"
    >
      <p class="font-semibold text-(--color-heading)">아직 등록된 태그가 없습니다.</p>
      <p class="mt-2 text-sm text-(--color-text-secondary)">게시글에 태그가 추가되면 이곳에서 찾아볼 수 있습니다.</p>
    </div>
    <ul v-else class="grid grid-cols-1 gap-3 sm:grid-cols-2">
      <li v-for="tag in tags" :key="tag.tagName">
        <TagCard :tagName="tag.tagName" :postCount="tag.postCount" />
      </li>
    </ul>
  </BlogLayout>
</template>
