<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { LayoutList } from 'lucide-vue-next'
import { useRoute, useRouter } from 'vue-router'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import BlogHeader from '@/components/layout/BlogHeader.vue'
import TagPostCard from '@/components/blog/TagPostCard.vue'
import { fetchCategoryPosts, type CategoryPosts } from '@/api/categories'
import type { PostSummary } from '@/api/posts'

const route = useRoute()
const router = useRouter()

const categoryName = computed(() => route.params.categoryName as string)
const category = ref<CategoryPosts | null>(null)
const isLoading = ref(true)
const error = ref<string | null>(null)
let requestId = 0

function formatDate(createAt: string) {
  return new Date(createAt.replace(' ', 'T')).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  })
}

const postsByYear = computed(() =>
  Object.entries(
    (category.value?.posts ?? []).reduce<Record<string, PostSummary[]>>((groups, post) => {
      const year = String(new Date(post.createAt.replace(' ', 'T')).getFullYear())
      ;(groups[year] ??= []).push(post)
      return groups
    }, {}),
  )
    .sort(([a], [b]) => Number(b) - Number(a))
    .map(([year, posts]) => [
      year,
      posts.sort(
        (a, b) =>
          new Date(b.createAt.replace(' ', 'T')).getTime() -
          new Date(a.createAt.replace(' ', 'T')).getTime(),
      ),
    ]),
)

watch(
  categoryName,
  async (name) => {
    const currentRequestId = ++requestId
    category.value = null
    error.value = null
    isLoading.value = true

    try {
      const result = await fetchCategoryPosts(name)
      if (currentRequestId !== requestId) return
      if (result === null) {
        router.replace({ name: 'not-found' })
        return
      }
      category.value = result
    } catch (e) {
      if (currentRequestId !== requestId) return
      error.value = e instanceof Error ? e.message : '카테고리를 불러오지 못했습니다.'
    } finally {
      if (currentRequestId === requestId) {
        isLoading.value = false
      }
    }
  },
  { immediate: true },
)
</script>

<template>
  <BlogLayout>
    <BlogHeader
      :icon="LayoutList"
      :title="category?.categoryName"
      :description="category?.categoryDesc"
    />

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p
      v-else-if="error"
      class="rounded-lg border border-(--color-error) bg-(--color-surface) p-4 text-sm text-(--color-error)"
    >
      카테고리 게시글을 불러오지 못했습니다. 잠시 후 다시 시도해주세요. {{ error }}
    </p>

    <div
      v-else-if="category?.posts.length === 0"
      class="rounded-lg border border-(--color-border) bg-(--color-surface) px-6 py-12 text-center"
    >
      <p class="font-semibold text-(--color-heading)">이 카테고리의 게시글이 없습니다.</p>
      <p class="mt-2 text-sm text-(--color-text-secondary)">
        다른 카테고리나 전체 글 목록을 살펴보세요.
      </p>
    </div>
    <section
      v-for="([year, yearPosts], index) in postsByYear"
      v-else
      :key="year"
      class="mb-10 w-full"
    >
      <div class="mb-2 flex items-center gap-3">
        <h2 class="text-2xl font-black text-(--color-heading) text-center">{{ year }}</h2>
        <span
          class="translate-y-0.5 rounded-full border border-(--color-border) px-2.5 py-0.5 text-center text-xs font-medium text-(--color-text-secondary)"
        >
          {{ yearPosts.length }} Posts
        </span>
      </div>

      <ul class="flex flex-col">
        <li v-for="post in yearPosts" :key="post.id">
          <TagPostCard :title="post.title" :date="formatDate(post.createAt)" :id="post.id" />
        </li>
      </ul>
      <hr v-if="index < postsByYear.length - 1" class="mt-8 border-(--color-border)" />
    </section>
  </BlogLayout>
</template>
