<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { Trash2, X } from 'lucide-vue-next'

const props = defineProps<{
  open: boolean
  postId: string
  title?: string
  isDeleting?: boolean
  error?: string | null
}>()

const emit = defineEmits<{
  confirm: []
  close: []
}>()

const dialogRef = ref<HTMLDialogElement | null>(null)
const inputValue = ref('')
const inputRef = ref<HTMLInputElement | null>(null)

const canConfirm = computed(() => inputValue.value.trim() === props.postId && !props.isDeleting)

watch(
  () => props.open,
  (isOpen) => {
    const dialog = dialogRef.value
    if (!dialog) return
    if (isOpen) {
      inputValue.value = ''
      if (!dialog.open) dialog.showModal()
      inputRef.value?.focus()
    } else if (dialog.open) {
      dialog.close()
    }
  },
  { flush: 'post' },
)

onMounted(() => {
  if (props.open) dialogRef.value?.showModal()
})

function handleSubmit() {
  if (canConfirm.value) emit('confirm')
}

function handleBackdropClick(event: MouseEvent) {
  if (event.target === dialogRef.value && !props.isDeleting) emit('close')
}

function handleNativeClose() {
  if (props.open) emit('close')
}
</script>

<template>
  <dialog
    ref="dialogRef"
    aria-labelledby="delete-post-title"
    class="m-auto max-h-[calc(100dvh-2rem)] w-[calc(100%-2rem)] max-w-md overflow-y-auto rounded-xl border border-(--color-border) bg-(--color-surface) p-0 text-(--color-text) shadow-xl backdrop:bg-black/50"
    @cancel.prevent="!isDeleting && emit('close')"
    @close="handleNativeClose"
    @click="handleBackdropClick"
  >
    <form class="relative p-6 sm:p-8" @submit.prevent="handleSubmit">
      <button
        type="button"
        aria-label="닫기"
        :disabled="isDeleting"
        class="absolute top-4 right-4 inline-flex h-11 w-11 items-center justify-center rounded-lg text-(--color-text-secondary) transition-colors hover:bg-(--color-background-soft) hover:text-(--color-text) focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-(--color-primary)"
        @click="emit('close')"
      >
        <X class="h-5 w-5" />
      </button>

      <div class="mb-6 flex h-12 w-12 items-center justify-center rounded-lg bg-(--color-accent-soft) text-(--color-error)">
        <Trash2 class="h-6 w-6" />
      </div>
      <h2 id="delete-post-title" class="text-xl font-bold text-(--color-heading)">
        게시글을 삭제하시겠습니까?
      </h2>
      <p v-if="title" class="mt-2 break-words text-sm font-semibold text-(--color-text)">
        {{ title }}
      </p>
      <p class="mt-2 text-sm text-(--color-text-secondary)">
        삭제하면 되돌릴 수 없습니다. 계속하려면 게시글 ID
        <span class="font-semibold text-(--color-text)">{{ postId }}</span>
        를 입력하세요.
      </p>

      <input
        ref="inputRef"
        v-model="inputValue"
        type="text"
        autocomplete="off"
        :placeholder="postId"
        aria-label="삭제할 게시글 ID"
        class="mt-4 min-h-11 w-full rounded-lg border border-(--color-border) bg-(--color-background) px-3 text-sm text-(--color-text) focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-(--color-primary)"
      />

      <p v-if="error" class="mt-3 text-sm text-(--color-error)" role="alert">{{ error }}</p>

      <div class="mt-8 flex flex-col-reverse justify-end gap-3 sm:flex-row">
        <button
          type="button"
          :disabled="isDeleting"
          class="min-h-11 rounded-lg border border-(--color-border) bg-(--color-surface) px-5 text-sm font-medium text-(--color-text) transition-colors hover:border-(--color-border-hover) hover:bg-(--color-background-soft) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary)"
          @click="emit('close')"
        >
          취소
        </button>
        <button
          type="submit"
          :disabled="!canConfirm"
          class="min-h-11 rounded-lg bg-(--color-error) px-5 text-sm font-semibold text-white transition-opacity hover:opacity-90 focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-error) disabled:cursor-not-allowed disabled:opacity-50"
        >
          {{ isDeleting ? '삭제 중...' : '삭제' }}
        </button>
      </div>
    </form>
  </dialog>
</template>
