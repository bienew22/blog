<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import BlogHeader from '@/components/layout/BlogHeader.vue'
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
  ).sort(([a], [b]) => Number(b) - Number(a))
  .map(([year, groupPosts]) => [
    year, groupPosts.sort((a, b) => new Date(b.createAt.replace(' ', 'T')).getTime() - new Date(a.createAt.replace(' ', 'T')).getTime())
  ])
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
    <BlogHeader
      :icon="Book"
      title="POSTS"
      description="배우고, 만들고, 고민한 것들을 기록합니다."
    />

    <div v-if="isLoading" class="flex min-h-[50vh] items-center justify-center">
      <div
        class="h-10 w-10 animate-spin rounded-full border-4 border-(--color-border) border-t-(--color-heading)"
      ></div>
    </div>
    <p v-else-if="error" class="rounded-lg border border-(--color-error) bg-(--color-surface) p-4 text-sm text-(--color-error)">
      게시글을 불러오지 못했습니다. 잠시 후 다시 시도해주세요. {{ error }}
    </p>

    <div
      v-else-if="posts.length === 0"
      class="rounded-lg border border-(--color-border) bg-(--color-surface) px-6 py-12 text-center"
    >
      <p class="font-semibold text-(--color-heading)">아직 게시글이 없습니다.</p>
      <p class="mt-2 text-sm text-(--color-text-secondary)">새 글이 등록되면 이곳에서 확인할 수 있습니다.</p>
    </div>

    <section v-for="([year, yearPosts], index) in postsByYear" :key="year" class="mb-10 w-full">
      <div class="mb-2 flex items-center gap-3">
        <h2 class="text-xl font-bold text-(--color-heading)">{{ year }}</h2>
        <span
          class="rounded-full border border-(--color-border) px-2.5 py-0.5 text-xs font-medium text-(--color-text-muted)"
        >
          {{ yearPosts.length }} Posts
        </span>
      </div>

      <ul class="flex flex-col">
        <li v-for="post in yearPosts" :key="post.id" class="flex items-start">
          <PostCard
            :title="post.title"
            :date="formatDate(post.createAt)"
            :tags="post.tags"
            :post-id="post.id"
          />
        </li>
      </ul>
      <hr v-if="index < postsByYear.length - 1" class="mt-8 border-(--color-border)" />
    </section>
  </BlogLayout>
</template>
