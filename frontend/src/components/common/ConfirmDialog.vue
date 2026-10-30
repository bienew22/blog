<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'

const props = withDefaults(
  defineProps<{
    open: boolean
    title: string
    message?: string
    confirmText?: string
    cancelText?: string
  }>(),
  { confirmText: '확인', cancelText: '취소' },
)

const emit = defineEmits<{
  confirm: []
  close: []
}>()

const dialogRef = ref<HTMLDialogElement | null>(null)

watch(
  () => props.open,
  (isOpen) => {
    const dialog = dialogRef.value
    if (!dialog) return
    if (isOpen) {
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
    aria-labelledby="confirm-dialog-title"
    class="m-auto w-[calc(100%-2rem)] max-w-md rounded-xl border border-(--color-border) bg-(--color-surface) p-0 text-(--color-text) shadow-xl backdrop:bg-black/50"
    @cancel.prevent="emit('close')"
    @close="handleNativeClose"
    @click="handleBackdropClick"
  >
    <section class="p-6 sm:p-8">
      <h2 id="confirm-dialog-title" class="text-xl font-bold text-(--color-heading)">{{ title }}</h2>
      <p v-if="message" class="mt-2 text-sm text-(--color-text-secondary)">{{ message }}</p>
      <div class="mt-8 flex flex-col-reverse justify-end gap-3 sm:flex-row">
        <button
          type="button"
          class="min-h-11 rounded-lg border border-(--color-border) bg-(--color-surface) px-5 text-sm font-medium text-(--color-text) transition-colors hover:border-(--color-border-hover) hover:bg-(--color-background-soft) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary) hover:cursor-pointer"
          @click="emit('close')"
        >
          {{ cancelText }}
        </button>
        <button
          type="button"
          class="min-h-11 rounded-lg bg-(--color-primary) px-5 text-sm font-semibold text-white transition-colors hover:bg-(--color-primary-hover) focus-visible:outline-3 focus-visible:outline-offset-3 focus-visible:outline-(--color-primary) hover:cursor-pointer"
          @click="emit('confirm')"
        >
          {{ confirmText }}
        </button>
      </div>
    </section>
  </dialog>
</template>
