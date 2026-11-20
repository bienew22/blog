<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { apiFetch } from '@/api/client'

interface UploadResult {
  id: number
  name: string
  status: string
  response: string
}

const results = ref<UploadResult[]>([])
const message = ref('')
const dragging = ref(false)
let nextId = 0

function clipboardFile(file: File): File {
  const extensions: Record<string, string> = {
    'image/jpeg': 'jpg',
    'image/png': 'png',
    'image/gif': 'gif',
  }
  const extension = extensions[file.type]
  if (file.name.includes('.') || !extension) return file
  return new File([file], `${file.name || 'clipboard'}.${extension}`, { type: file.type })
}

async function upload(file: File) {
  const result = ref<UploadResult>({
    id: nextId++,
    name: file.name,
    status: '업로드 중',
    response: '',
  })
  results.value.unshift(result.value)

  const formData = new FormData()
  formData.append('file', file)

  try {
    const response = await apiFetch('/api/v1/images', { method: 'POST', body: formData })
    const body = await response.text()
    const entry = results.value.find((item) => item.id === result.value.id)
    if (entry) {
      entry.status = `${response.ok ? '완료' : '실패'} (HTTP ${response.status})`
      entry.response = body || '(응답 본문 없음)'
    }
  } catch (error) {
    const entry = results.value.find((item) => item.id === result.value.id)
    if (entry) {
      entry.status = '요청 실패'
      entry.response = error instanceof Error ? error.message : String(error)
    }
  }
}

function uploadFiles(files: File[], fromClipboard = false) {
  if (!files.length) {
    message.value = '첨부된 파일이 없습니다.'
    return
  }
  const images = files.filter((file) => file.type.startsWith('image/'))
  message.value = images.length === files.length ? '' : '이미지 파일만 업로드할 수 있습니다.'
  images.forEach((file) => void upload(fromClipboard ? clipboardFile(file) : file))
}

function onFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  if (input.files?.length) uploadFiles(Array.from(input.files))
  input.value = ''
}

function onPaste(event: ClipboardEvent) {
  const files = Array.from(event.clipboardData?.files ?? [])
  if (!files.length) return
  event.preventDefault()
  uploadFiles(files, true)
}

function onDrop(event: DragEvent) {
  dragging.value = false
  uploadFiles(Array.from(event.dataTransfer?.files ?? []))
}

function onDragLeave(event: DragEvent) {
  if (event.currentTarget instanceof HTMLElement && event.relatedTarget instanceof Node &&
      event.currentTarget.contains(event.relatedTarget)) return
  dragging.value = false
}

onMounted(() => window.addEventListener('paste', onPaste))
onUnmounted(() => window.removeEventListener('paste', onPaste))
</script>

<template>
  <main class="mx-auto max-w-3xl px-6 py-12">
    <h1 class="text-2xl font-bold">이미지 업로드 테스트</h1>
    <p class="mt-3 text-sm text-(--color-text-secondary)">
      이미지를 붙여넣거나 아래 영역에 드래그하여 놓으면 즉시 업로드합니다.
      백엔드는 JPG, PNG, GIF 파일을 최대 10MB까지 허용합니다.
    </p>
    <p class="mt-2 text-sm text-(--color-text-secondary)">
      업로드에는 로그인이 필요합니다.
      <RouterLink class="text-(--color-primary) underline" :to="{ name: 'login', query: { redirect: '/dev/image-upload' } }">
        로그인
      </RouterLink>
    </p>

    <div
      class="mt-6 rounded-xl border-2 border-dashed p-10 text-center"
      :class="dragging ? 'border-(--color-primary)' : 'border-(--color-border)'"
      @dragenter.prevent="dragging = true"
      @dragover.prevent
      @dragleave.prevent="onDragLeave"
      @drop.prevent="onDrop"
    >
      <p>이미지를 여기에 놓거나 파일을 선택하세요.</p>
      <input
        class="mt-4 max-w-full text-sm"
        type="file"
        accept="image/*"
        multiple
        aria-label="업로드할 이미지 선택"
        @change="onFileChange"
      />
    </div>

    <p v-if="message" role="alert" class="mt-4 text-sm text-red-600">{{ message }}</p>

    <section class="mt-8" aria-live="polite">
      <h2 class="text-lg font-semibold">응답</h2>
      <p v-if="!results.length" class="mt-2 text-sm text-(--color-text-secondary)">아직 업로드한 이미지가 없습니다.</p>
      <div v-for="result in results" :key="result.id" class="mt-4 rounded-lg border border-(--color-border) p-4">
        <p class="break-all font-medium">{{ result.name }}</p>
        <p class="mt-1 text-sm">{{ result.status }}</p>
        <pre v-if="result.response" class="mt-3 overflow-x-auto whitespace-pre-wrap break-all text-sm">{{ result.response }}</pre>
      </div>
    </section>
  </main>
</template>
