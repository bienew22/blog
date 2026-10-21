<script setup lang="ts">
import Tooltip from './Tooltip.vue'
import { useThemeStore } from '@/stores/theme'
import { LightThemeIcon, DarkThemeIcon } from '@/components/icon'

const theme = useThemeStore()
</script>

<template>
    <button
        type="button"
        class="group relative flex h-11 w-11 items-center justify-center rounded-lg border border-(--color-border) bg-(--color-surface) text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
        aria-label="테마 전환"
        @click="theme.toggleTheme"
    >
        <component :is="theme.isDark ? LightThemeIcon : DarkThemeIcon" class="h-4 w-4" />

        <Tooltip content="Theme" />

    </button>
</template>