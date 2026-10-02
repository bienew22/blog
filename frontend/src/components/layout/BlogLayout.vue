<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { ChevronUp } from 'lucide-vue-next'
import BlogSidebar from './BlogSidebar.vue'

const showScrollTop = ref(false)

function handleScroll() {
  showScrollTop.value = window.scrollY > 300
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => window.addEventListener('scroll', handleScroll, { passive: true }))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<template>
  <div class="flex min-h-screen flex-col bg-(--color-background) text-(--color-text) lg:flex-row">
    <BlogSidebar />
    <main class="flex min-h-screen min-w-0 flex-1 flex-col px-4 py-6 lg:px-10">
      <div class="mx-auto w-full min-w-0 flex-1 px-0 py-6 sm:py-8">
        <slot />
      </div>
    </main>

    <Transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0 translate-y-2"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100 translate-y-0"
      leave-to-class="opacity-0 translate-y-2"
    >
      <button
        v-if="showScrollTop"
        type="button"
        aria-label="맨 위로 이동"
        class="fixed right-6 bottom-6 z-50 flex h-11 w-11 items-center justify-center rounded-full bg-(--color-background-soft) text-(--color-text) shadow-md transition-all duration-300 hover:-translate-y-1 hover:bg-(--color-background-mute)"
        @click="scrollToTop"
      >
        <ChevronUp class="h-5 w-5" />
      </button>
    </Transition>
  </div>
</template>
