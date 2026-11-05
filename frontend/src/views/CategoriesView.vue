<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { LayoutGrid } from 'lucide-vue-next'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import BlogHeader from '@/components/layout/BlogHeader.vue'
import AllPostsBanner from '@/components/category/AllPostsBanner.vue'
import CategoryCard from '@/components/category/CategoryCard.vue'
import { fetchCategories, type CategorySummary } from '@/api/categories'

const categories = ref<CategorySummary[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)

const totalPostCount = computed(() =>
  categories.value.reduce((sum, category) => sum + category.postCount, 0),
)

onMounted(async () => {
  try {
    categories.value = await fetchCategories()
  } catch (e) {
    error.value = e instanceof Error ? e.message : '카테고리를 불러오지 못했습니다.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <BlogLayout>
    <BlogHeader
      :icon="LayoutGrid"
      title="CATEGORIES"
      description="주제별로 모아 보는 글입니다. 관심 있는 카테고리를 골라 보세요."
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
      카테고리를 불러오지 못했습니다. 잠시 후 다시 시도해주세요. {{ error }}
    </p>
    <div
      v-else-if="categories.length === 0"
      class="rounded-lg border border-(--color-border) bg-(--color-surface) px-6 py-12 text-center"
    >
      <p class="font-semibold text-(--color-heading)">아직 등록된 카테고리가 없습니다.</p>
    </div>
    <template v-else>
      <AllPostsBanner :categoryCount="categories.length" :totalPostCount="totalPostCount" />
      <ul class="grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3">
        <li v-for="(category, i) in categories" :key="category.categoryName">
          <CategoryCard
            :index="i + 1"
            :categoryName="category.categoryName"
            :categoryDesc="category.categoryDesc"
            :postCount="category.postCount"
            :lastPostCreateAt="category.lastPostCreateAt"
          />
        </li>
      </ul>
    </template>
  </BlogLayout>
</template>
