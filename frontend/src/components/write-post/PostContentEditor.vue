<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { Paperclip } from 'lucide-vue-next'
import { apiFetch } from '@/api/client'

const content = defineModel<string>({ required: true })
const emit = defineEmits<{
    toast: [message: string, type: 'pending' | 'success' | 'error', duration: number]
}>()

const editor = ref<HTMLTextAreaElement | null>(null)
const fileInput = ref<HTMLInputElement | null>(null)
const attachButton = ref<HTMLButtonElement | null>(null)
const mirror = ref<HTMLTextAreaElement | null>(null)
const lineNumberGutter = ref<HTMLDivElement | null>(null)
const contentLines = computed(() => content.value.split('\n'))
const defaultLineHeight = 28
const lineHeights = ref<number[]>([])
let resizeObserver: ResizeObserver | null = null

// 화면상 줄바꿈(soft wrap)된 높이를 미러 요소로 측정해 줄 번호 높이에 반영
function measureLines() {
    const el = mirror.value
    const textarea = editor.value
    if (!el || !textarea) return

    if (textarea.clientWidth === 0) return // 숨겨진 탭에서는 측정하지 않음

    el.style.width = `${textarea.offsetWidth}px`
    el.style.height = '0px'
    const style = getComputedStyle(textarea)
    const lineHeight = Number.parseFloat(style.lineHeight) || defaultLineHeight
    const padding = (Number.parseFloat(style.paddingTop) || 0) + (Number.parseFloat(style.paddingBottom) || 0)

    // 줄마다 값을 넣어 실제 표시 줄 수를 구하고, 줄 높이의 정수배로 보정
    lineHeights.value = contentLines.value.map((line) => {
        el.value = line
        const rows = Math.max(1, Math.round((el.scrollHeight - padding) / lineHeight))
        return rows * lineHeight
    })
}

watch(content, () => nextTick(measureLines), { flush: 'post' })

onMounted(() => {
    measureLines()
    if (editor.value) {
        resizeObserver = new ResizeObserver(measureLines)
        resizeObserver.observe(editor.value)
    }
})

onBeforeUnmount(() => resizeObserver?.disconnect())

function lineHeightAt(index: number) {
    return lineHeights.value[index] || defaultLineHeight
}

function syncLineNumberScroll() {
    nextTick(() => {
        if (editor.value && lineNumberGutter.value) {
            lineNumberGutter.value.scrollTop = editor.value.scrollTop
        }
    })
}

function scrollCaretIntoView(textarea: HTMLTextAreaElement) {
    const style = window.getComputedStyle(textarea)

    const paddingTop = Number.parseFloat(style.paddingTop) || 0
    const paddingBottom = Number.parseFloat(style.paddingBottom) || 0
    const lineIndex = textarea.value.slice(0, textarea.selectionStart).split('\n').length - 1
    let offset = 0
    for (let i = 0; i < lineIndex; i++) offset += lineHeightAt(i)
    const lineTop = paddingTop + offset
    const lineBottom = lineTop + lineHeightAt(lineIndex)
    const visibleTop = textarea.scrollTop + paddingTop
    const visibleBottom = textarea.scrollTop + textarea.clientHeight - paddingBottom

    if (lineBottom > visibleBottom) {
        textarea.scrollTop = lineBottom - textarea.clientHeight + paddingBottom
    } else if (lineTop < visibleTop) {
        textarea.scrollTop = lineTop - paddingTop
    }

    syncLineNumberScroll()
}

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

async function uploadImage(file: File): Promise<string> {
    const formData = new FormData()
    formData.append('file', file)
    const response = await apiFetch('/api/v1/images', { method: 'POST', body: formData })
    if (!response.ok) throw new Error(`HTTP ${response.status}`)

    const data: unknown = await response.json()
    if (typeof data !== 'object' || data === null || !('url' in data) ||
        typeof data.url !== 'string' || !data.url.trim()) {
        throw new Error('이미지 URL이 응답에 없습니다.')
    }
    return data.url
}

async function attachImages(files: File[]) {
    if (!files.length) return
    const images = files.filter((file) => file.type.startsWith('image/'))
    const errors = files.length === images.length ? [] : ['이미지 파일만 첨부할 수 있습니다.']
    if (!images.length) {
        emit('toast', errors.join(' '), 'error', 3000)
        return
    }

    emit('toast', '이미지 업로드 중...', 'pending', 2000)
    const results = await Promise.allSettled(images.map(uploadImage))
    const urls: string[] = []
    results.forEach((result, index) => {
        if (result.status === 'fulfilled') {
            urls.push(result.value)
        } else {
            const reason = result.reason instanceof Error ? result.reason.message : String(result.reason)
            errors.push(`${images[index]?.name || '이미지'} 업로드 실패: ${reason}`)
        }
    })
    if (errors.length) emit('toast', errors.join(' '), 'error', 3000)

    const textarea = editor.value
    if (!textarea || !urls.length) return
    const position = textarea.selectionStart
    const markdown = urls.map((url) => `![](${url})`).join('\n')
    textarea.setRangeText(markdown, position, position, 'end')
    content.value = textarea.value
    textarea.setSelectionRange(position + 2, position + 2)
    if (document.activeElement === fileInput.value || document.activeElement === attachButton.value) {
        textarea.focus()
    }
    nextTick(() => syncLineNumberScroll())
    emit('toast', '이미지 업로드 완료', 'success', 2000)
}

function onFileChange(event: Event) {
    const input = event.target as HTMLInputElement
    const files = Array.from(input.files ?? [])
    input.value = ''
    void attachImages(files)
}

/**
 * 클립보드에서 이미지 붙여넣기 처리
 */
function onPaste(event: ClipboardEvent) {
    const files = Array.from(event.clipboardData?.files ?? [])
    if (!files.some((file) => file.type.startsWith('image/'))) return
    event.preventDefault()
    void attachImages(files.map(clipboardFile))
}

/**
 * 이미지 드래그 오버 이벤트 처리
 */
function onDragOver(event: DragEvent) {
    if (event.dataTransfer?.types.includes('Files')) event.preventDefault()
}

/**
 * 이미지 드래그 드롭 이벤트 처리
 */
function onDrop(event: DragEvent) {
    if (!event.dataTransfer?.types.includes('Files')) return
    event.preventDefault()
    void attachImages(Array.from(event.dataTransfer.files))
}

/**
 * Tab / Shift+Tab 키 입력 처리
 */
function handleTab(e: KeyboardEvent) {
    const textarea = e.target as HTMLTextAreaElement
    const start = textarea.selectionStart
    const end = textarea.selectionEnd
    const value = textarea.value


    // 선택 영역이 포함하는 줄의 시작 인덱스와 끝 인덱스 계산
    const lineStart = value.lastIndexOf('\n', start - 1) + 1
    let lineEnd = value.indexOf('\n', end)

    if (lineEnd === -1) {   // 마지막줄을 선택한 경우
        lineEnd = value.length
    }

    // 선택된 줄 전체 텍스트
    const selectedText = value.slice(lineStart, lineEnd)
    const lines = selectedText.split('\n')

    // Case 1: 텍스트 선택 없는 경우 -> 단순 탭 삽입
    if (start === end && !e.shiftKey) {
        textarea.setRangeText('\t', start, end, 'end')
        content.value = textarea.value
        return
    }

    // Case 2: Shift + Tab (내어쓰기)
    if (e.shiftKey) {
        let removedFirstLineTab = false // 첫 번째 라인에서 탭이 제거되었는지 여부
        let totalRemovedTabs = 0        // 전체 제거된 탭 개수

        const newLines = lines.map((line, index) => {
            if (line.startsWith('\t')) {
                if (index === 0) removedFirstLineTab = true
                totalRemovedTabs++
                return line.substring(1)
            }
            return line
        })

        const newText = newLines.join('\n')
        textarea.setRangeText(newText, lineStart, lineEnd, 'preserve')
        content.value = textarea.value

        // 실제 제거된 탭 개수만큼만 선택 영역 및 커서 보정
        const newStart = Math.max(lineStart, start - (removedFirstLineTab ? 1 : 0))
        const newEnd = Math.max(newStart, end - totalRemovedTabs)

        nextTick(() => {
            textarea.setSelectionRange(newStart, newEnd)
        })
    }
    // Case 3: Tab (선택 영역 들여쓰기)
    else {
        const newText = lines.map(line => '\t' + line).join('\n')
        textarea.setRangeText(newText, lineStart, lineEnd, 'preserve')
        content.value = textarea.value

        // 모든 줄마다 \t(1자)가 추가되었으므로 위치 보정
        const newStart = start + 1
        const newEnd = end + lines.length

        nextTick(() => {
            textarea.setSelectionRange(newStart, newEnd)
        })
    }
}

/**
 * enter 키 입력 시 처리
 *
 * 바로 윗줄의 들여쓰기 수준을 그대로 가져옴
 */
function handleEnter(e: KeyboardEvent) {
    e.preventDefault()

    const textarea = e.target as HTMLTextAreaElement

    const value = textarea.value
    const cursor = textarea.selectionStart

    // 현재 줄 시작 위치
    const lineStart = value.lastIndexOf('\n', cursor - 1) + 1

    // 현재 줄 끝 위치
    let lineEnd = value.indexOf('\n', lineStart)

    if (lineEnd == -1) {
        lineEnd = value.length
    }

    // 현재줄에 '\t' 시작 개수
    const match = value.slice(lineStart, lineEnd).match(/^\t*/)

    const indent = match ? match[0] : ''

    const insertText = '\n' + indent

    textarea.setRangeText(
        insertText,
        cursor,
        cursor,
        'end'
    )

    content.value = textarea.value
    nextTick(() => scrollCaretIntoView(textarea))
}
</script>

<template>
  <section>
    <div class="mb-3 flex items-center justify-between">
      <h2 class="text-sm font-semibold text-(--color-heading)">본문</h2>
      <span class="text-xs text-(--color-text-secondary)">{{ content.length }}자</span>
    </div>

    <div role="toolbar" aria-label="본문 편집 도구" class="mb-2 flex flex-wrap items-center gap-1 rounded-lg border border-(--color-border) bg-(--color-surface) p-2">
      <button
        ref="attachButton"
        type="button"
        aria-label="이미지 첨부"
        title="이미지 첨부"
        class="inline-flex h-9 w-9 items-center justify-center rounded-md border border-(--color-border) text-(--color-text) hover:cursor-pointer hover:border-(--color-primary) hover:text-(--color-primary)"
        @click="fileInput?.click()"
      >
        <Paperclip aria-hidden="true" class="h-4 w-4" />
      </button>
      <input ref="fileInput" type="file" accept="image/*" multiple class="hidden" @change="onFileChange" />
    </div>

    <div class="relative flex h-[80vh] min-h-0 w-full overflow-hidden rounded-lg border border-(--color-border) bg-(--color-surface)" @dragover="onDragOver" @drop="onDrop">
      <div
          ref="lineNumberGutter"
          aria-hidden="true"
          class="pointer-events-none h-full w-12 shrink-0 select-none overflow-hidden border-r border-(--color-border-subtle) bg-(--color-background-soft) pb-0 pt-4 text-right font-mono text-sm leading-7 text-(--color-text-muted)"
      >
          <div
              v-for="(_, index) in contentLines"
              :key="index"
              class="pr-2"
              :style="{ height: `${lineHeightAt(index)}px` }"
          >
              {{ index + 1 }}
          </div>
      </div>

      <!-- 줄 높이 측정용 숨김 textarea (실제 입력창과 동일한 스타일로 줄바꿈을 계산) -->
      <textarea
          ref="mirror"
          aria-hidden="true"
          tabindex="-1"
          readonly
          wrap="soft"
          class="my-scroll pointer-events-none invisible absolute top-0 left-12 -z-10 box-border resize-none overflow-x-hidden overflow-y-scroll whitespace-pre-wrap break-words border-0 pb-0 pl-3 pr-4 pt-4 font-mono text-sm leading-7"
      ></textarea>

      <!-- 입력 영역 -->
      <textarea
          ref="editor"
          v-model="content"
          placeholder="본문을 작성하세요"
          wrap="soft"
          class="my-scroll h-full min-h-0 w-full min-w-0 flex-1 resize-none overflow-x-hidden overflow-y-scroll whitespace-pre-wrap break-words bg-transparent pb-0 pl-3 pr-4 pt-4 font-mono text-sm leading-7 text-(--color-text) outline-none placeholder:text-(--color-text-muted)"
          @scroll="syncLineNumberScroll"
          @input="syncLineNumberScroll"
          @paste="onPaste"
          @keydown.tab.prevent="handleTab"
          @keydown.enter="handleEnter"
      ></textarea>
    </div>
  </section>
</template>