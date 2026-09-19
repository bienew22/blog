<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { Book } from 'lucide-vue-next'
import { fetchPosts, type Post } from '@/api/posts'
import PostCard from '@/components/blog/PostCard.vue'

const posts = ref<Post[]>([])
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
    posts.value.reduce<Record<string, Post[]>>((groups, post) => {
      const year = String(new Date(post.createAt.replace(' ', 'T')).getFullYear())
      ;(groups[year] ??= []).push(post)
      return groups
    }, {}),
  ).sort(([a], [b]) => Number(b) - Number(a)),
)

onMounted(async () => {
  try {
    posts.value = await fetchPosts()
  } catch (e) {
    error.value = e instanceof Error ? e.message : '게시글을 불러오지 못했습니다.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <BlogLayout>
    <header class="mb-10">
      <h1 class="flex items-center text-center gap-3 text-4xl font-bold text-(--color-heading)">
        <Book class="h-9 w-9" />
        POSTS
      </h1>
      <p class="mt-2 text-sm text-(--color-text-secondary)">
        &nbsp;배우고, 만들고, 고민한 것들을 기록합니다.
      </p>
    </header>

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="text-sm text-red-500">{{ error }}</p>

    <section v-for="([year, yearPosts], index) in postsByYear" :key="year" class="mb-10 w-full">
      <div class="mb-2 flex items-center gap-3">
        <h2 class="text-2xl font-black text-(--color-heading) text-center">{{ year }}</h2>
        <span
          class="rounded-full border border-(--color-border) px-2.5 py-0.5 translate-y-0.5 text-xs text-center font-medium text-(--color-text-secondary)"
        >
          {{ yearPosts.length }} posts
        </span>
      </div>

      <ul class="flex flex-col">
        <li v-for="post in yearPosts" :key="post.slug" class="flex items-start">
          <PostCard
            :title="post.title"
            :date="formatDate(post.createAt)"
            :tags="post.tags"
            :href="post.slug"
          />
        </li>
      </ul>
      <hr v-if="index < postsByYear.length - 1" class="mt-8 border-(--color-border)" />
    </section>
  </BlogLayout>
</template>
