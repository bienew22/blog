<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { ref, onMounted, computed, watch, nextTick } from 'vue'
import { fetchPostDetail, type PostDetail } from '@/api/posts'
import { useRoute, useRouter, RouterLink } from 'vue-router'
import { NotebookText } from 'lucide-vue-next'
import Prism from 'prismjs'

import 'prismjs/components/prism-typescript'
import 'prismjs/components/prism-jsx'
import 'prismjs/components/prism-tsx'
import 'prismjs/components/prism-bash'
import 'prismjs/components/prism-json'
import 'prismjs/components/prism-yaml'
import 'prismjs/components/prism-python'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-sql'
import 'prismjs/components/prism-git'
import 'prismjs/components/prism-diff'
import 'prismjs/components/prism-docker'
import 'prismjs/components/prism-markdown'
import 'prismjs/plugins/line-numbers/prism-line-numbers'

Prism.manual = true

const route = useRoute()
const router = useRouter()

const slug = computed(() => route.params.slug as string)

const post = ref<PostDetail | null>(null)
const isLoading = ref(true)
const error = ref<string | null>(null)

const contentRef = ref<HTMLElement | null>(null)

const formattedDate = computed(() => {
  if (!post.value) return ''
  return new Date(post.value.createAt.replace(' ', 'T')).toLocaleDateString('en-US', {
    month: 'long',
    day: 'numeric',
    year: 'numeric',
  })
})

onMounted(async () => {
  try {
    post.value = await fetchPostDetail(slug.value)
    if (post.value === null) {
      router.replace({ name: 'not-found' })
    }
  } catch (e) {
    error.value = e instanceof Error ? e.message : '포스트를 불러오지 못했습니다.'
  } finally {
    isLoading.value = false
  }
})

watch(
  () => post.value?.content,
  async () => {
    await nextTick()

    if (contentRef.value) {
      Prism.highlightAllUnder(contentRef.value)

      contentRef.value.querySelectorAll('pre').forEach((pre) => {
        if (pre.parentElement?.classList.contains('code-window')) return

        const code = pre.querySelector('code')
        if (!code) return

        const languageClass = [...code.classList].find((className) =>
          className.startsWith('language-'),
        )
        const language = languageClass?.replace('language-', '') ?? 'code'
        const languageNames: Record<string, string> = {
          bash: 'Shell',
          sh: 'Shell',
          shell: 'Shell',
          text: 'Text',
          plaintext: 'Text',
        }

        const frame = document.createElement('div')
        frame.className = 'code-window'

        const toolbar = document.createElement('div')
        toolbar.className = 'code-window__toolbar'

        // 코드 블럭 신호등 추가
        const trafficLights = document.createElement('div')
        trafficLights.className = 'code-window__lights'
        trafficLights.setAttribute('aria-hidden', 'true')
        for (const color of ['red', 'yellow', 'green']) {
          const light = document.createElement('span')
          light.className = `code-window__light code-window__light--${color}`
          trafficLights.append(light)
        }

        // 코드 블럭 제목 추가
        const title = document.createElement('span')
        title.className = 'code-window__title'
        title.innerHTML = '<span aria-hidden="true">&lt;/&gt;</span>'
        title.append(document.createTextNode(` ${languageNames[language] ?? language}`))

        // 코드 블럭 복사 버튼 추가
        const copyButton = document.createElement('button')
        copyButton.className = 'code-window__copy'
        copyButton.type = 'button'
        copyButton.textContent = 'Copy'
        copyButton.setAttribute('aria-label', 'Copy code')
        copyButton.title = 'Copy code'
        copyButton.addEventListener('click', async () => {
          await navigator.clipboard.writeText(code.textContent ?? '')
          copyButton.textContent = 'Copied'
          window.setTimeout(() => {
            copyButton.textContent = 'Copy'
          }, 1500)
        })

        toolbar.append(trafficLights, title, copyButton)
        pre.before(frame)
        frame.append(toolbar, pre)
      })
    }
  },
)
</script>

<template>
  <BlogLayout>
    <!-- <BlogHeader :icon="TagIcon" :title="post?.title" :description="post?.content" /> -->

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="text-sm text-red-500">{{ error }}</p>
    <div v-else class="max-w-none">
      <header class="mb-8">
        <!-- 블로그 포스트 헤더 : 아이콘 -->
        <NotebookText class="mb-1 h-12 w-12 text-(--color-heading)" :stroke-width="1.5" />
        <!-- 블로그 포스트 헤더 : 메타 정보 -->
        <div class="ml-1 flex items-center gap-3 font-mono text-sm text-(--color-text-secondary)">
          <time>{{ formattedDate }}</time>
        </div>
        <!-- 블로그 포스트 헤더 : 제목 -->
        <h1 class="mt-1 text-3xl leading-tight font-bold text-(--color-heading)">
          {{ post?.title }}
        </h1>
        <!-- 블로그 포스트 헤더 : 태그 -->
        <ul v-if="post?.tags?.length" class="mt-3 flex flex-wrap gap-1">
          <li v-for="tag in post.tags" :key="tag">
            <RouterLink
              :to="`/tag/${tag}`"
              class="inline-block rounded-md border-2 border-(--color-border) bg-(--color-background-soft) px-3 py-1 font-mono text-sm text-(--color-heading) transition-colors hover:bg-(--color-background-mute) hover:border-(--color-border-hover)"
            >
              <span class="text-blue-600 dark:text-blue-400">#</span>{{ tag }}
            </RouterLink>
          </li>
        </ul>
      </header>
      <div ref="contentRef" class="post-content" v-html="post?.content"></div>
    </div>
  </BlogLayout>
</template>
