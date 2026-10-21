<script setup lang="ts">
/**
 * @component Tooltip
 * @description 마우스 호버 시 툴팁을 표시하는 컴포넌트입니다.
 * 
 * [사용법 (Usage)]
 * 부모 요소에 반드시 `relative`와 `group` 클래스가 있어야 합니다.
 * 
 * @example
 * <div class="relative group inline-block">
 *   <button>버튼</button>
 *   <Tooltip content="툴팁 텍스트" />
 * </div>
 */

defineProps<{
  content: string
}>()
</script>

<template>
    <span
        class="pointer-events-none absolute -top-7 left-1/2 -translate-x-1/2 rounded bg-(--color-surface-elevated-reverse) px-1.5 py-0.5 text-xs whitespace-nowrap text-(--color-text-reverse) opacity-0 transition duration-500 after:absolute after:top-full after:left-1/2 after:-translate-x-1/2 after:border-4 after:border-transparent after:border-t-(--color-surface-elevated-reverse) after:content-[''] group-hover:opacity-100"
    >
        {{content}}
    </span>
</template>