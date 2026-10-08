<script setup lang="ts">
import { nextTick, ref } from 'vue'

const content = defineModel<string>({ required: true })

const editor = ref<HTMLTextAreaElement | null>(null)


/**
 * tab 입려 시 처리
 *
 * - tab 선택된 모든 줄 앞에 들여쓰기 
 * - shift + tab : 선택된 모든 줄 내어쓰기
 */
function handleTab (e: KeyboardEvent) {
    const textarea = e.target as HTMLTextAreaElement

    const start = textarea.selectionStart
    const end = textarea.selectionEnd

    const value =  textarea.value

    // 선택 영역의 시작 위치
    const lineStart = value.lastIndexOf('\n', start - 1) + 1

    // 선택 영역의 마지막 줄의 끝 위치
    let lineEnd = value.indexOf('\n', end)

    // 마지막 줄 선택한 경우
    if (lineEnd == -1) {
        lineEnd = value.length
    }

    // 선택된 모든 줄
    const selectedText = value.slice(lineStart, lineEnd)

    const lines = selectedText.split('\n')

    // 내어쓰기
    if (e.shiftKey) {

        // 기존 라인에서 앞에 있는 '\t' 제거
        const newLines = lines.map(line => {
            if (line.startsWith('\t')) {
                return line.substring(1)
            }

            return line
        })

        const newText = newLines.join('\n')

        textarea.setRangeText(
            newText,
            lineStart,
            lineEnd,
            'select'
        )

        content.value = textarea.value

        // 선택 영역 유지
        textarea.selectionStart = start -1
        textarea.selectionEnd = start - 1 + newText.length
    }
    // 들여쓰기
    else {
        const newText = lines
            .map(line => '\t' + line)
            .join('\n')

        textarea.setRangeText(
            newText,
            lineStart,
            lineEnd,
            'select'
        )

        content.value = textarea.value

        //
        textarea.selectionStart = start + 1
        textarea.selectionEnd = end + lines.length
    }
}


/**
 * enter 키 입력 시 처리
 *
 * 바로 윗줄의 들여쓰기 수준을 그대로 가져옴
 */
function handleEnter(e: KeyboardEvent) {
    const textarea = e.target as HTMLTextAreaElement

    const value = textarea.value
    const cursor = textarea.selectionStart

    // 현재 줄 시작 위치
    const lineStart = value.lastIndexOf('\n', cursor - 1) + 1

    // 바로 윗줄의 시작 위치
    const previousLinedEnd = lineStart - 1

    // 첫 줄인 경우
    if (previousLinedEnd < 0) {
        return
    }

    const previousLineStart = value.lastIndexOf('\n', previousLinedEnd - 1) + 1

    const previousLine = value.slice(previousLineStart, previousLinedEnd)

    const match = previousLine.match(/^\t*/)

    const indent = match ? match[0] : ''

    // 선택 영역이 존재하는 경우 기본 Enter 동작을 사용
    if (textarea.selectionStart != textarea.selectionEnd) {
        return
    }

    e.preventDefault()

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