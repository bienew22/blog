<script setup lang="ts">
import BlogLayout from '@/components/layout/BlogLayout.vue'
import BlogHeader from '@/components/layout/BlogHeader.vue'
import { ref, onMounted, computed } from 'vue'
import { Tag as TagIcon } from 'lucide-vue-next'
import { fetchTagPosts, type TagPosts } from '@/api/tags'
import type { PostSummary } from '@/api/posts'
import TagPostCard from '@/components/blog/TagPostCard.vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const tagName = computed(() => route.params.tagName as string)

const tags = ref<TagPosts | null>(null)
const isLoading = ref(true)
const error = ref<string | null>(null)

function formatDate(createAt: string) {
  return new Date(createAt.replace(' ', 'T')).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  })
}

const postsByYear = computed(() =>
  Object.entries(
    (tags.value?.posts ?? []).reduce<Record<string, PostSummary[]>>((groups, post) => {
      const year = String(new Date(post.createAt.replace(' ', 'T')).getFullYear())
      ;(groups[year] ??= []).push(post)
      return groups
    }, {}),
  ).sort(([a], [b]) => Number(b) - Number(a)),
)

onMounted(async () => {
  try {
    tags.value = await fetchTagPosts(tagName.value)
    if (tags.value === null) {
      router.replace({ name: 'not-found' })
    }
  } catch (e) {
    error.value = e instanceof Error ? e.message : '태그를 불러오지 못했습니다.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <BlogLayout>
    <BlogHeader :icon="TagIcon" :title="tags?.tagName" :description="tags?.tagDesc" />

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="text-sm text-red-500">{{ error }}</p>

    <section
      v-for="([year, yearPosts], index) in postsByYear"
      v-else
      :key="year"
      class="mb-10 w-full"
    >
      <div class="mb-2 flex items-center gap-3">
        <h2 class="text-2xl font-black text-(--color-heading) text-center">{{ year }}</h2>
        <span
          class="rounded-full border border-(--color-border) px-2.5 py-0.5 translate-y-0.5 text-xs text-center font-medium text-(--color-text-secondary)"
        >
          {{ yearPosts.length }} Posts
        </span>
      </div>

      <ul class="flex flex-col">
        <li v-for="post in yearPosts" :key="post.slug">
          <TagPostCard :title="post.title" :date="formatDate(post.createAt)" :slug="post.slug" />
        </li>
      </ul>
      <hr v-if="index < postsByYear.length - 1" class="mt-8 border-(--color-border)" />
    </section>
  </BlogLayout>
</template>
