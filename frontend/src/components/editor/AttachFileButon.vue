<script setup lang="ts">
import { Paperclip } from 'lucide-vue-next'
import Tooltip from '../common/Tooltip.vue'

</script>

<template>
    <button
        ref="attachButton"
        type="button"
        aria-label="이미지 첨부"
        class="relative group inline-flex h-9 w-9 items-center justify-center rounded-md border border-(--color-border) text-(--color-text) hover:cursor-pointer hover:border-(--color-primary) hover:text-(--color-primary)"
    >
        <Paperclip aria-hidden="true" class="h-4 w-4" />
        <Tooltip content="이미지 첨부" />
    </button>
</template>
