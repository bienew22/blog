<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { FileText, Plus, RotateCcw, X } from 'lucide-vue-next'
import type { Post } from '@/api/posts'

const props = defineProps<{
  open: boolean
  drafts: Post[]
  isLoading: boolean
  error: string | null
}>()

const emit = defineEmits<{
  resume: [postId: string]
  newPost: []
  retry: []
  close: []
}>()

const selectedDraft = ref<Post | null>(null)

const dialogRef = ref<HTMLDialogElement | null>(null)

// showModal() provides focus trapping, inert background, and focus restoration on close
watch(
  () => props.open,
  (isOpen) => {
    const dialog = dialogRef.value
    if (!dialog) return
    if (isOpen) {
      selectedDraft.value = null
      if (!dialog.open) dialog.showModal()
    } else if (dialog.open) {
      dialog.close()
    }
  },
  { flush: 'post' },
)

onMounted(() => {
  if (props.open) dialogRef.value?.showModal()
})

function formatDate(createAt: string) {
  return new Date(createAt.replace(' ', 'T')).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}

// Esc steps back from the confirm view first, then closes the dialog
function handleEscape() {
  if (selectedDraft.value) selectedDraft.value = null
  else emit('close')
}

function handleBackdropClick(event: MouseEvent) {
  if (event.target === dialogRef.value) emit('close')
}

function handleNativeClose() {
  if (props.open) emit('close')
}
</script>

<template>
  <dialog
    ref="dialogRef"
    :aria-labelledby="selectedDraft ? 'resume-draft-title' : 'draft-list-title'"
    class="m-auto max-h-[calc(100dvh-2rem)] w-[calc(100%-2rem)] max-w-xl overflow-y-auto rounded-xl border border-(--color-border) bg-(--color-surface) p-0 text-(--color-text) shadow-xl backdrop:bg-black/50"
    @cancel.prevent="handleEscape"
    @close="handleNativeClose"
    @click="handleBackdropClick"
  >
      <section
        class="relative p-6 sm:p-8"
      >
        <button
          type="button"
          aria-label="닫기"
          class="absolute top-4 right-4 inline-flex h-11 w-11 items-center justify-center rounded-lg text-(--color-text-secondary) transition-colors hover:bg-(--color-background-soft) hover:text-(--color-text) focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-(--color-primary)"
          @click="emit('close')"
        >
          <X class="h-5 w-5" />
        </button>

        <template v-if="selectedDraft">
          <div class="mb-6 flex h-12 w-12 items-center justify-center rounded-lg bg-(--color-accent-soft) text-(--color-primary)">
            <FileText class="h-6 w-6" />
          </div>
          <h2 id="resume-draft-title" class="text-xl font-bold text-(--color-heading)">
            이어서 작성하시겠습니까?
          </h2>
          <p class="mt-2 break-words text-sm text-(--color-text-secondary)">
            <span class="font-semibold text-(--color-text)">{{ selectedDraft.title || '제목 없음' }}</span>
            글 작성을 이어갑니다.
          </p>
          <div class="mt-8 flex flex-col-reverse justify-end gap-3 sm:flex-row">
            <button
              type="button"
              class="min-h-11 rounded-lg border border-(--color-border) bg-(--color-surface) px-5 text-sm font-medium text-(--color-text) transition-colors hover:border-(--color-border-hover) hover:bg-(--color-background-soft) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary)"
              @click="selectedDraft = null"
            >
              취소
            </button>
            <button
              type="button"
              class="min-h-11 rounded-lg bg-(--color-primary) px-5 text-sm font-semibold text-white transition-colors hover:bg-(--color-primary-hover) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary)"
              @click="emit('resume', selectedDraft.id)"
            >
              이어서 작성
            </button>
          </div>
        </template>

        <template v-else>
          <div class="mb-6">
            <p class="text-xs font-semibold tracking-widest text-(--color-primary)">WRITE</p>
            <h2 id="draft-list-title" class="mt-2 text-2xl font-bold text-(--color-heading)">
              임시저장한 글
            </h2>
            <p class="mt-2 text-sm text-(--color-text-secondary)">
              이어서 작성할 글을 선택하거나 새로운 글을 작성하세요.
            </p>
          </div>

          <div v-if="isLoading" class="flex min-h-36 items-center justify-center" role="status">
            <span class="sr-only">임시저장한 글을 불러오는 중입니다.</span>
            <div class="h-9 w-9 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-primary)" />
          </div>

          <div
            v-else-if="error"
            class="rounded-lg border border-(--color-error) bg-(--color-surface) p-4"
            role="alert"
          >
            <p class="text-sm text-(--color-error)">임시저장한 글을 불러오지 못했습니다.</p>
            <p class="mt-1 break-words text-xs text-(--color-text-secondary)">{{ error }}</p>
            <button
              type="button"
              class="mt-3 inline-flex min-h-10 items-center gap-2 rounded-lg border border-(--color-border) px-3 text-sm font-medium text-(--color-text) hover:bg-(--color-background-soft) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary)"
              @click="emit('retry')"
            >
              <RotateCcw class="h-4 w-4" />
              다시 시도
            </button>
          </div>

          <div
            v-else-if="drafts.length === 0"
            class="rounded-lg border border-(--color-border-subtle) bg-(--color-background-soft) px-5 py-8 text-center"
          >
            <p class="font-semibold text-(--color-heading)">임시저장한 글이 없습니다.</p>
            <p class="mt-2 text-sm text-(--color-text-secondary)">새 글을 작성해보세요.</p>
          </div>

          <ul v-else class="my-scroll max-h-[min(50vh,24rem)] space-y-2 overflow-y-auto pr-1">
            <li v-for="draft in drafts" :key="draft.id">
              <button
                type="button"
                class="group flex min-h-16 w-full items-center gap-3 rounded-lg border border-(--color-border-subtle) bg-(--color-surface) px-4 py-3 text-left transition-colors hover:border-(--color-primary) hover:bg-(--color-background-soft) focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-(--color-primary)"
                @click="selectedDraft = draft"
              >
                <FileText class="h-5 w-5 shrink-0 text-(--color-primary)" />
                <span class="min-w-0 flex-1">
                  <span class="block break-words font-medium text-(--color-heading) group-hover:text-(--color-primary)">
                    {{ draft.title || '제목 없음' }}
                  </span>
                  <time
                    class="mt-1 block text-xs text-(--color-text-muted)"
                    :datetime="draft.createAt.replace(' ', 'T')"
                  >
                    {{ formatDate(draft.createAt) }}
                  </time>
                </span>
              </button>
            </li>
          </ul>

          <div class="mt-6 border-t border-(--color-border-subtle) pt-5">
            <button
              type="button"
              class="inline-flex min-h-11 w-full items-center justify-center gap-2 rounded-lg bg-(--color-primary) px-5 text-sm font-semibold text-white transition-colors hover:bg-(--color-primary-hover) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary)"
              @click="emit('newPost')"
            >
              <Plus class="h-4 w-4" />
              새로운 글 작성
            </button>
          </div>
        </template>
      </section>
  </dialog>
</template>
