<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import WriteTab from '@/components/write-post/WriteTab.vue'
import PreviewTab from '@/components/write-post/PreviewTab.vue'
import DraftPostDialog from '@/components/write-post/DraftPostDialog.vue'
import { Eye, Pencil, Save, Send } from 'lucide-vue-next'
import { type PostStatus, POST_STATUS } from '@/types/postStatus'
import { type Tag } from '@/api/tags'
import { createPost, fetchDraftPosts, fetchPostForEdit, updatePost, type Post } from '@/api/posts'

const route = useRoute()
const router = useRouter()
const postId = ref(route.params.id as string | undefined)
const title = ref('')
const content = ref('')
const activeTab = ref<'edit' | 'preview'>('edit')
const selectedTags = ref<Tag[]>([])
const isLoadingPost = ref(!!postId.value)
const postLoadError = ref(false)
const drafts = ref<Post[]>([])
const isLoadingDrafts = ref(false)
const draftLoadError = ref<string | null>(null)
const isDraftDialogOpen = ref(!postId.value)

type ToastType = 'pending' | 'success' | 'error'
const toasts = ref<{ id: number; message: string; type: ToastType }[]>([])
const isSavingPost = ref(false) // 연속 클릭을 막음
let toastSeq = 0  // toast의 아이디

watch(
  () => route.params.id,
  async (routePostId, _old, onCleanup) => {
    let isStale = false
    onCleanup(() => {
      isStale = true
    })
    postId.value = typeof routePostId === 'string' ? routePostId : undefined

    if (!postId.value) {
      title.value = ''
      content.value = ''
      selectedTags.value = []
      isLoadingPost.value = false
      postLoadError.value = false
      isDraftDialogOpen.value = true
      await loadDrafts()
      return
    }

    isDraftDialogOpen.value = false
    isLoadingPost.value = true
    postLoadError.value = false
    try {
      const post = await fetchPostForEdit(postId.value)
      if (isStale) return
      title.value = post.title
      content.value = post.contentMd
      selectedTags.value = post.tags.map((tagName) => ({ tagName, postCount: 0 }))
    } catch (e) {
      if (isStale) return
      postLoadError.value = true
      showToast(e instanceof Error ? e.message : '게시글을 불러오지 못했습니다.', 'error', 3000)
    } finally {
      if (!isStale) isLoadingPost.value = false
    }
  },
  { immediate: true },
)

async function loadDrafts() {
  isLoadingDrafts.value = true
  draftLoadError.value = null
  try {
    drafts.value = await fetchDraftPosts()
    if (drafts.value.length === 0) isDraftDialogOpen.value = false
  } catch (e) {
    draftLoadError.value = e instanceof Error ? e.message : '임시저장한 글을 불러오지 못했습니다.'
  } finally {
    isLoadingDrafts.value = false
  }
}

function startNewPost() {
  isDraftDialogOpen.value = false
  postId.value = undefined
  title.value = ''
  content.value = ''
  selectedTags.value = []
}

async function resumeDraft(id: string) {
  isDraftDialogOpen.value = false
  await router.push({ name: 'edit-post', params: { id } })
}

async function closeDraftDialog() {
  await router.push({ name: 'home' })
}

function dismissToast(id: number) {
  toasts.value = toasts.value.filter((t) => t.id !== id)
}

function showToast(message: string, type: ToastType, duration: number) {
  const id = ++toastSeq
  toasts.value.push({ id, message, type })
  if (duration) setTimeout(() => dismissToast(id), duration)
  return id
}

async function savePost(option: boolean, status: PostStatus) {
  if (isLoadingPost.value || postLoadError.value) return

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
    const tags = selectedTags.value.map((tag) => tag.tagName)
    const post = postId.value
      ? await updatePost(postId.value, postTitle, content.value, tags, status)
      : await createPost(postTitle, content.value, tags, status)
    if (!post.id) {
      throw new Error('저장된 게시글 ID를 응답에서 찾을 수 없습니다.')
    }
    postId.value = post.id
    showToast('게시글 저장 완료', 'success', 2000)

    if (option) {
      await router.push({ name: 'post', params: { id: post.id } })
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
    <div v-if="isLoadingPost || (isLoadingDrafts && isDraftDialogOpen)" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
        role="status"
        aria-label="불러오는 중"
      ></div>
    </div>
    <form v-else-if="!isDraftDialogOpen" class="mx-auto w-full" @submit.prevent>
      <div class="mb-4 flex items-center justify-end gap-1.5 sm:gap-2">
        <button
          type="button"
          class="inline-flex h-9 items-center justify-center gap-1 rounded-lg border border-(--color-border) bg-(--color-surface) px-2 text-xs font-medium whitespace-nowrap text-(--color-text) transition-colors hover:border-(--color-primary) hover:text-(--color-primary) sm:gap-2 sm:px-4 sm:text-sm hover:cursor-pointer"
          :disabled="isSavingPost || isLoadingPost || postLoadError"
          @click="savePost(false, POST_STATUS.DRAFT)"
        >
          <Save class="h-4 w-4 shrink-0" />
          임시저장
        </button>
        <button
          type="button"
          class="inline-flex h-9 items-center justify-center gap-1 rounded-lg bg-(--color-primary) px-2 text-xs font-semibold whitespace-nowrap text-white transition-colors hover:bg-(--color-primary-hover) disabled:cursor-not-allowed disabled:opacity-60 sm:gap-2 sm:px-4 sm:text-sm hover:cursor-pointer"
          :disabled="isSavingPost || isLoadingPost || postLoadError"
          @click="savePost(true, POST_STATUS.PUBLISHED)"
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
          class="inline-flex h-11 items-center justify-center gap-2 border-b-2 text-sm font-medium whitespace-nowrap transition-colors hover:cursor-pointer"
          :class="activeTab === 'edit' ? 'border-(--color-primary) text-(--color-primary)' : 'border-transparent text-(--color-text-secondary)'"
          @click="activeTab = 'edit'"
        >
          <Pencil class="h-4 w-4" />작성
        </button>
        <button
          type="button"
          role="tab"
          :aria-selected="activeTab === 'preview'"
          class="inline-flex h-11 items-center justify-center gap-2 border-b-2 text-sm font-medium whitespace-nowrap transition-colors hover:cursor-pointer"
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

    <DraftPostDialog
      :open="isDraftDialogOpen"
      :drafts="drafts"
      :is-loading="isLoadingDrafts"
      :error="draftLoadError"
      @resume="resumeDraft"
      @new-post="startNewPost"
      @retry="loadDrafts"
      @close="closeDraftDialog"
    />
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
