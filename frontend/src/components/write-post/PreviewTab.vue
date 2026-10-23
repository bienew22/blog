<script setup lang="ts">
import { onMounted, ref } from 'vue'
import PostView from '@/views/PostView.vue'
import { fetchPostPreview, type PostDetail } from '@/api/posts'
import type { Tag } from '@/api/tags'

const props = defineProps<{
  title: string
  content: string
  tags: Tag[]
}>()

const emit = defineEmits<{
  error: [message: string]
}>()

const previewPost = ref<PostDetail | null>(null)
const isLoading = ref(false)

// 탭이 표시될 때마다(v-if로 마운트) 최신 내용으로 미리보기를 불러온다.
onMounted(async () => {
  isLoading.value = true
  try {
    previewPost.value = await fetchPostPreview(
      props.title.trim(),
      props.content,
      props.tags.map((tag) => tag.tagName),
    )
  } catch (e) {
    emit('error', e instanceof Error ? e.message : '미리보기를 불러오지 못했습니다.')
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <div role="tabpanel">
    <div v-if="isLoading" class="flex min-h-[30vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <PostView v-else-if="previewPost" :preview="previewPost" />
  </div>
</template>
