<script setup lang="ts">
import { nextTick, ref } from 'vue'

const content = defineModel<string>({ required: true })

const editor = ref<HTMLTextAreaElement | null>(null)


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
}
</script>

<template>
  <section>
    <div class="mb-3 flex items-center justify-between">
      <h2 class="text-sm font-semibold text-(--color-heading)">본문</h2>
      <span class="text-xs text-(--color-text-secondary)">{{ content.length }}자</span>
    </div>
    <textarea
      ref="editor"
      v-model="content"
      placeholder="본문을 작성하세요"
      class="min-h-[55vh] w-full resize-y rounded-md border border-(--color-border) bg-(--color-background) p-4 leading-7 text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-secondary) focus:border-(--color-hover-title) dark:border-(--color-border-hover) dark:bg-[#29292f]"
      @keydown.tab.prevent="handleTab"
      @keydown.enter="handleEnter"
    ></textarea>
  </section>
</template>