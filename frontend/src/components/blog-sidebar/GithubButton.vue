<script setup lang="ts">
import Tooltip from './Tooltip.vue'
import { GithubIcon } from '@/components/icon'

</script>

<template>
    <a
        href="https://github.com/bienew22"
        target="_blank"
        rel="noopener noreferrer"
        class="group relative flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
        aria-label="GitHub"
    >
        <GithubIcon class="h-4 w-4" />
            
        <Tooltip content="Github" />
    </a>
</template>