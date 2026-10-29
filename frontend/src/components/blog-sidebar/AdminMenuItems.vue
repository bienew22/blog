<script setup lang="ts">
import { PenLine } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
import Tooltip from './Tooltip.vue'
// 관리자에게만 보이는 메뉴. 항목을 추가하려면 template에 링크/버튼을 늘리면 됩니다.
</script>

<template>
  <RouterLink
    to="/write"
    aria-label="Write"
    class="group relative flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:cursor-pointer hover:border-(--color-primary) hover:text-(--color-primary)"
  >
    <PenLine class="h-4 w-4" />
    <Tooltip content="Write" />
  </RouterLink>
</template>
