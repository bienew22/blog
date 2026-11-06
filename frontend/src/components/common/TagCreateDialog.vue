<script setup lang="ts">
import { nextTick, onMounted, ref, watch } from 'vue'
import { X } from 'lucide-vue-next'

const props = defineProps<{
  open: boolean
  tagName: string
  isSubmitting?: boolean
}>()

const emit = defineEmits<{
  submit: [tagName: string, tagDesc: string]
  close: []
}>()

const dialogRef = ref<HTMLDialogElement | null>(null)
const nameRef = ref<HTMLInputElement | null>(null)
const descRef = ref<HTMLTextAreaElement | null>(null)
const tagDesc = ref('')
const name = ref('')
const nameError = ref('')
const descError = ref('')

async function openDialog() {
  const dialog = dialogRef.value
  if (!dialog || dialog.open) return
  name.value = props.tagName
  tagDesc.value = ''
  nameError.value = ''
  descError.value = ''
  dialog.showModal()
  await nextTick()
  descRef.value?.focus()
}

watch(
  () => props.open,
  (isOpen) => {
    if (isOpen) openDialog()
    else if (dialogRef.value?.open) dialogRef.value.close()
  },
  { flush: 'post' },
)

onMounted(() => {
  if (props.open) openDialog()
})

function handleSubmit() {
  const trimmedName = name.value.trim()
  const trimmedDesc = tagDesc.value.trim()
  if (props.isSubmitting) return

  nameError.value = trimmedName ? '' : '태그 이름을 입력해주세요.'
  descError.value = trimmedDesc ? '' : '태그 설명을 입력해주세요.'
  if (nameError.value) {
    nameRef.value?.focus()
    return
  }
  if (descError.value) {
    descRef.value?.focus()
    return
  }
  emit('submit', trimmedName, trimmedDesc)
}

function handleNativeClose() {
  if (props.open) emit('close')
}
</script>

<template>
  <dialog
    ref="dialogRef"
    aria-labelledby="tag-create-title"
    class="m-auto max-h-[calc(100dvh-2rem)] w-[calc(100%-2rem)] max-w-md overflow-y-auto rounded-xl border border-(--color-border) bg-(--color-surface) p-0 text-(--color-text) shadow-xl backdrop:bg-black/50"
    @cancel.prevent="emit('close')"
    @close="handleNativeClose"
  >
    <form class="relative p-6 sm:p-8" novalidate @submit.prevent="handleSubmit">
      <button
        type="button"
        aria-label="닫기"
        class="absolute top-4 right-4 inline-flex h-11 w-11 items-center justify-center rounded-lg text-(--color-text-secondary) transition-colors hover:bg-(--color-background-soft) hover:text-(--color-text) focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-(--color-primary) hover:cursor-pointer"
        @click="emit('close')"
      >
        <X class="h-5 w-5" />
      </button>

      <h2 id="tag-create-title" class="text-xl font-bold text-(--color-heading)">태그 추가</h2>

      <div class="mt-6 flex flex-wrap items-baseline gap-x-3">
        <label class="text-sm font-semibold text-(--color-heading)" for="tag-create-name">
          태그 이름 <span class="text-(--color-error)" aria-hidden="true">*</span>
        </label>
        <p v-if="nameError" id="tag-create-name-error" role="alert" class="text-xs text-(--color-error)">
          {{ nameError }}
        </p>
      </div>
      <input
        id="tag-create-name"
        ref="nameRef"
        v-model="name"
        type="text"
        maxlength="100"
        placeholder="태그 이름을 입력하세요"
        :aria-invalid="!!nameError"
        :aria-describedby="nameError ? 'tag-create-name-error' : undefined"
        class="mt-2 h-11 w-full rounded-lg border bg-(--color-surface) px-3 text-sm text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-muted) focus:border-(--color-primary)"
        :class="nameError ? 'border-(--color-error)' : 'border-(--color-border)'"
        @input="nameError = ''"
      />

      <div class="mt-5 flex flex-wrap items-baseline gap-x-3">
        <label class="text-sm font-semibold text-(--color-heading)" for="tag-create-desc">
          태그 설명 <span class="text-(--color-error)" aria-hidden="true">*</span>
        </label>
        <p v-if="descError" id="tag-create-desc-error" role="alert" class="text-xs text-(--color-error)">
          {{ descError }}
        </p>
      </div>
      <textarea
        id="tag-create-desc"
        ref="descRef"
        v-model="tagDesc"
        rows="4"
        maxlength="500"
        placeholder="태그 설명을 입력하세요"
        :aria-invalid="!!descError"
        :aria-describedby="descError ? 'tag-create-desc-error' : undefined"
        class="mt-2 w-full resize-none rounded-lg border bg-(--color-surface) px-3 py-2 text-sm text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-muted) focus:border-(--color-primary)"
        :class="descError ? 'border-(--color-error)' : 'border-(--color-border)'"
        @input="descError = ''"
      />

      <div class="mt-8 flex flex-col-reverse justify-end gap-3 sm:flex-row">
        <button
          type="button"
          class="min-h-11 rounded-lg border border-(--color-border) bg-(--color-surface) px-5 text-sm font-medium text-(--color-text) transition-colors hover:border-(--color-border-hover) hover:bg-(--color-background-soft) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary) hover:cursor-pointer"
          @click="emit('close')"
        >
          취소
        </button>
        <button
          type="submit"
          :disabled="isSubmitting"
          class="min-h-11 rounded-lg bg-(--color-primary) px-5 text-sm font-semibold text-white transition-colors hover:bg-(--color-primary-hover) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary) disabled:opacity-60 hover:cursor-pointer"
        >
          저장
        </button>
      </div>
    </form>
  </dialog>
</template>
