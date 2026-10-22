<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import WriteTab from '@/components/write-post/WriteTab.vue'
import PreviewTab from '@/components/write-post/PreviewTab.vue'
import { Eye, Pencil, Save, Send } from 'lucide-vue-next'
import { type Tag } from '@/api/tags'
import { createPost } from '@/api/posts'

const router = useRouter()
const title = ref('')
const content = ref('')
const activeTab = ref<'edit' | 'preview'>('edit')
const selectedTags = ref<Tag[]>([])

type ToastType = 'pending' | 'success' | 'error'
const toasts = ref<{ id: number; message: string; type: ToastType }[]>([])
const isSavingPost = ref(false)
let toastSeq = 0  // toast의 아이디

function dismissToast(id: number) {
  toasts.value = toasts.value.filter((t) => t.id !== id)
}

function showToast(message: string, type: ToastType, duration: number) {
  const id = ++toastSeq
  toasts.value.push({ id, message, type })
  if (duration) setTimeout(() => dismissToast(id), duration)
  return id
}

async function savePost(option: boolean) {
  const postTitle = title.value.trim()
  const postContent = content.value.trim()

  if (!postTitle) {
    showToast('제목을 입력해주세요.', 'error', 2000)
    return
  }

  if (!postContent) {
    showToast('내용을 입력해주세요.', 'error', 2000)
    return
  }

  if (isSavingPost.value) return

  isSavingPost.value = true
  showToast('게시글 저장 중...', 'pending', 2000)

  try {
    const post = await createPost(
      postTitle,
      content.value,
      selectedTags.value.map((tag) => tag.tagName),
    )
    showToast('게시글 저장 완료', 'success', 2000)

    if (option) {
      await router.push({ name: 'post', params: { slug: post.slug } })
    }
  } catch (e) {
    showToast(e instanceof Error ? e.message : '게시글 저장에 실패했습니다.', 'error', 3000)
  } finally {
    isSavingPost.value = false
  }
}
</script>

<template>
  <BlogLayout>
    <form class="mx-auto w-full" @submit.prevent>
      <div class="mb-4 flex items-center justify-end gap-1.5 sm:gap-2">
        <button
          type="button"
          class="inline-flex h-9 items-center justify-center gap-1 rounded-lg border border-(--color-border) bg-(--color-surface) px-2 text-xs font-medium whitespace-nowrap text-(--color-text) transition-colors hover:border-(--color-primary) hover:text-(--color-primary) sm:gap-2 sm:px-4 sm:text-sm"
        >
          <Save class="h-4 w-4 shrink-0" />
          임시저장
        </button>
        <button
          type="button"
          class="inline-flex h-9 items-center justify-center gap-1 rounded-lg bg-(--color-primary) px-2 text-xs font-semibold whitespace-nowrap text-white transition-colors hover:bg-(--color-primary-hover) disabled:cursor-not-allowed disabled:opacity-60 sm:gap-2 sm:px-4 sm:text-sm"
          :disabled="isSavingPost"
          @click="savePost(true)"
        >
          <Send class="h-4 w-4 shrink-0" />
          저장
        </button>
      </div>

      <div class="mb-6 grid grid-cols-2 border-b border-(--color-border-subtle)" role="tablist">
        <button
          type="button"
          role="tab"
          :aria-selected="activeTab === 'edit'"
          class="inline-flex h-11 items-center justify-center gap-2 border-b-2 text-sm font-medium whitespace-nowrap transition-colors"
          :class="activeTab === 'edit' ? 'border-(--color-primary) text-(--color-primary)' : 'border-transparent text-(--color-text-secondary)'"
          @click="activeTab = 'edit'"
        >
          <Pencil class="h-4 w-4" />작성
        </button>
        <button
          type="button"
          role="tab"
          :aria-selected="activeTab === 'preview'"
          class="inline-flex h-11 items-center justify-center gap-2 border-b-2 text-sm font-medium whitespace-nowrap transition-colors"
          :class="activeTab === 'preview' ? 'border-(--color-primary) text-(--color-primary)' : 'border-transparent text-(--color-text-secondary)'"
          @click="activeTab = 'preview'"
        >
          <Eye class="h-4 w-4" />미리보기
        </button>
      </div>

      <PreviewTab
        v-if="activeTab === 'preview'"
        :title="title"
        :content="content"
        :tags="selectedTags"
        @error="(message) => showToast(message, 'error', 3000)"
      />

      <WriteTab
        v-show="activeTab === 'edit'"
        v-model:title="title"
        v-model:content="content"
        v-model:selected-tags="selectedTags"
        @toast="showToast"
      />
    </form>

    <TransitionGroup
      name="toast"
      tag="div"
      class="pointer-events-none fixed bottom-20 right-8 z-50 flex flex-col items-end gap-2"
    >
      <div
        v-for="t in toasts"
        :key="t.id"
        role="status"
        class="w-72 overflow-hidden rounded-lg border border-(--color-border) bg-(--color-surface-elevated) px-5 py-3 text-sm font-medium whitespace-nowrap text-ellipsis shadow-md"
        :class="{
          'text-(--color-text-secondary)': t.type === 'pending',
          'text-(--color-success)': t.type === 'success',
          'text-(--color-error)': t.type === 'error',
        }"
      >
        {{ t.message }}
      </div>
    </TransitionGroup>
  </BlogLayout>
</template>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition:
    width 0.5s ease,
    padding 0.5s ease;
}

.toast-enter-from,
.toast-leave-to {
  width: 0;
  padding-left: 0;
  padding-right: 0;
}

.toast-move {
  transition: transform 0.5s ease;
}
</style>
