<script setup lang="ts">
import { useRouter } from 'vue-router'
import { PencilLine } from 'lucide-vue-next'
import Tooltip from './Tooltip.vue'

const router = useRouter()
</script>

<template>
    <button
        type="button"
        class="group relative flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
        aria-label="문서 작성"
        @click="router.push('/write')"
    >
        <PencilLine class="h-4 w-4" />

        <Tooltip content="Write" />

    </button>
</template>