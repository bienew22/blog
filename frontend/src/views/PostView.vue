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

const props = defineProps<{ preview?: PostDetail }>()

const postId = computed(() => route.params.id as string)

const post = ref<PostDetail | null>(null)
const isLoading = ref(!props.preview)
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

watch(
  () => props.preview,
  (value) => {
    if (value) post.value = value
  },
  { immediate: true },
)

onMounted(async () => {
  if (props.preview) return
  try {
    post.value = await fetchPostDetail(postId.value)
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
  { immediate: true },
)
</script>

<template>
  <component :is="preview ? 'div' : BlogLayout">
    <!-- <BlogHeader :icon="TagIcon" :title="post?.title" :description="post?.content" /> -->

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="rounded-lg border border-(--color-error) bg-(--color-surface) p-4 text-sm text-(--color-error)">
      게시글을 불러오지 못했습니다. 잠시 후 다시 시도해주세요. {{ error }}
    </p>
    <article v-else class="mx-auto w-full">
      <header class="mb-8 border-b border-(--color-border-subtle) pb-6 sm:mb-10 sm:pb-8">
        <!-- 블로그 포스트 헤더 : 아이콘 -->
        <NotebookText class="mb-4 h-8 w-8 text-(--color-primary)" :stroke-width="1.5" />
        <!-- 블로그 포스트 헤더 : 메타 정보 -->
        <div class="flex items-center gap-3 text-xs text-(--color-text-muted)">
          <time :datetime="post?.createAt.replace(' ', 'T')">{{ formattedDate }}</time>
        </div>
        <!-- 블로그 포스트 헤더 : 제목 -->
        <h1 class="mt-3 break-words text-[2rem] leading-tight font-bold tracking-tight text-(--color-heading) sm:text-[2.25rem]">
          {{ post?.title }}
        </h1>
        <!-- 블로그 포스트 헤더 : 태그 -->
        <ul v-if="post?.tags?.length" class="mt-5 flex flex-wrap gap-2">
          <li v-for="tag in post.tags" :key="tag">
            <RouterLink
              :to="`/tag/${tag}`"
              class="inline-block max-w-full break-all rounded-full border border-(--color-border) bg-(--color-surface) px-3 py-1 text-sm text-(--color-text-secondary) transition-colors hover:border-(--color-primary) hover:bg-(--color-accent-soft) hover:text-(--color-primary)"
            >
              <span class="text-(--color-primary)">#</span>{{ tag }}
            </RouterLink>
          </li>
        </ul>
      </header>
      <div ref="contentRef" class="post-content" v-html="post?.content"></div>
    </article>
  </component>
</template>
