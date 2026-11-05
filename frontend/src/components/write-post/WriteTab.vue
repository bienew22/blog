<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import PostContentEditor from '@/components/write-post/PostContentEditor.vue'
import TagCreateDialog from '@/components/common/TagCreateDialog.vue'
import CategoryCreateDialog from '@/components/common/CategoryCreateDialog.vue'
import { ChevronDown, Plus, Search } from 'lucide-vue-next'
import { fetchTags, createTag, type Tag } from '@/api/tags'
import { fetchCategoryOptions, createCategory, type CategoryOption } from '@/api/categories'

const title = defineModel<string>('title', { required: true })
const content = defineModel<string>('content', { required: true })
const selectedTags = defineModel<Tag[]>('selectedTags', { required: true })
const selectedCategoryId = defineModel<number | null>('selectedCategoryId', { required: true })

const emit = defineEmits<{
  toast: [message: string, type: 'pending' | 'success' | 'error', duration: number]
}>()

const availableTags = ref<Tag[]>([])
const tagSearch = ref('')
const isTagListOpen = ref(false)
const tagPicker = ref<HTMLElement | null>(null)
const isCreatingTag = ref(false) // 생성 완료전까지 중복 클릭 예방용
const isTagDialogOpen = ref(false)
const newTagName = ref('')
const categories = ref<CategoryOption[]>([])
const isLoadingCategories = ref(false)
const categoryLoadError = ref(false)
const isCategoryListOpen = ref(false)
const categoryPicker = ref<HTMLElement | null>(null)

const selectedCategoryName = computed(
  () => categories.value.find((c) => c.categoryId === selectedCategoryId.value)?.categoryName ?? null,
)
const isCategoryDisabled = computed(
  () => isLoadingCategories.value || categoryLoadError.value,
)
const isCategoryDialogOpen = ref(false)
const isCreatingCategory = ref(false)

function openCategoryDialog() {
  isCategoryListOpen.value = false
  isCategoryDialogOpen.value = true
}

async function addCategory(categoryName: string, categoryDesc: string) {
  if (isCreatingCategory.value) return

  isCreatingCategory.value = true
  emit('toast', '카테고리 생성 중...', 'pending', 2000)

  try {
    const created = await createCategory(categoryName, categoryDesc)
    categories.value.push(created)
    selectedCategoryId.value = created.categoryId
    isCategoryDialogOpen.value = false

    emit('toast', '카테고리 생성 완료', 'success', 2000)
  } catch (e) {
    emit('toast', e instanceof Error ? e.message : '카테고리 생성에 실패했습니다.', 'error', 2000)
  } finally {
    isCreatingCategory.value = false
  }
}

function selectCategory(category: CategoryOption) {
  selectedCategoryId.value = category.categoryId
  isCategoryListOpen.value = false
}

function closeCategoryListOnFocusout(event: FocusEvent) {
  const nextTarget = event.relatedTarget
  if (!(nextTarget instanceof Node) || !categoryPicker.value?.contains(nextTarget)) {
    isCategoryListOpen.value = false
  }
}

const filteredTags = computed(() => {
  const query = tagSearch.value.trim().toLocaleLowerCase()
  if (!query) return availableTags.value

  return availableTags.value.filter((tag) => tag.tagName.toLocaleLowerCase().includes(query))
})

onMounted(async () => {
  try {
    availableTags.value = await fetchTags()
  } catch (e) {
    emit('toast', '태그를 불러오지 못했습니다.', 'error', 2000)
  }
})

async function loadCategories() {
  isLoadingCategories.value = true
  categoryLoadError.value = false
  try {
    categories.value = await fetchCategoryOptions()
  } catch (e) {
    categoryLoadError.value = true
    emit('toast', e instanceof Error ? e.message : '카테고리를 불러오지 못했습니다.', 'error', 2000)
  } finally {
    isLoadingCategories.value = false
  }
}

onMounted(loadCategories)

function toggleTag(clickedTag: Tag) {
  const index = selectedTags.value.findIndex((tag) => tag.tagName === clickedTag.tagName)

  if (index >= 0) {
    selectedTags.value.splice(index, 1)
  } else {
    selectedTags.value.push(clickedTag)
  }

  tagSearch.value = ''
  isTagListOpen.value = false
}

function handleEsc(e: KeyboardEvent) {
  tagSearch.value = ''
  isTagListOpen.value = false

  ;(e.target as HTMLElement).blur()
}

function closeTagListOnFocusout(event: FocusEvent) {
  const nextTarget = event.relatedTarget
  if (!(nextTarget instanceof Node) || !tagPicker.value?.contains(nextTarget)) {
    isTagListOpen.value = false
  }
}

function openTagDialog() {
  const name = tagSearch.value.trim()
  if (!name) return
  newTagName.value = name
  isTagListOpen.value = false
  isTagDialogOpen.value = true
}

async function addTag(tagName: string, tagDesc: string) {
  if (!tagName || isCreatingTag.value) return

  isCreatingTag.value = true
  emit('toast', '태그 생성 중...', 'pending', 2000)

  try {
    const created = await createTag(tagName, tagDesc)

    availableTags.value.push(created)
    selectedTags.value.push(created)
    tagSearch.value = ''
    isTagDialogOpen.value = false

    emit('toast', '태그 생성 완료', 'success', 2000)
  } catch (e) {
    emit('toast', e instanceof Error ? e.message : '태그 생성에 실패했습니다.', 'error', 2000)
  } finally {
    isCreatingTag.value = false
  }
}
</script>

<template>
  <section class="space-y-5">

    <!-- 카테고리 선택 -->
    <section>
      <div class="flex items-center gap-3">
        <label for="post-category" class="w-16 shrink-0 text-sm font-semibold text-(--color-heading)">
          카테고리
        </label>
        <div ref="categoryPicker" class="relative min-w-0 flex-1" @focusout="closeCategoryListOnFocusout">
          <button
            id="post-category"
            type="button"
            role="combobox"
            aria-haspopup="listbox"
            aria-controls="available-category-list"
            :aria-expanded="isCategoryListOpen"
            :disabled="isCategoryDisabled"
            class="flex h-11 w-full items-center justify-between rounded-lg border border-(--color-border) bg-(--color-surface) pr-3 pl-4 text-left text-sm outline-none transition-colors hover:cursor-pointer focus:border-(--color-primary) disabled:cursor-not-allowed disabled:opacity-60"
            :class="selectedCategoryName ? 'text-(--color-text)' : 'text-(--color-text-muted)'"
            @click="isCategoryListOpen = !isCategoryListOpen"
            @keydown.esc="isCategoryListOpen = false"
          >
            <span class="truncate">
              {{
                selectedCategoryName ??
                (isLoadingCategories ? '카테고리를 불러오는 중...' : '카테고리를 선택하세요')
              }}
            </span>
            <ChevronDown
              class="h-4 w-4 shrink-0 text-(--color-text-secondary) transition-transform"
              :class="{ 'rotate-180': isCategoryListOpen }"
            />
          </button>
          <ul
            v-if="isCategoryListOpen"
            id="available-category-list"
            role="listbox"
            aria-label="카테고리 목록"
            class="my-scroll absolute top-full z-20 mt-1 max-h-56 w-full overflow-y-auto rounded-lg border border-(--color-border) bg-(--color-surface-elevated) py-1 shadow-md"
          >
            <li v-for="category in categories" :key="category.categoryId">
              <button
                type="button"
                role="option"
                :aria-selected="category.categoryId === selectedCategoryId"
                class="flex min-h-11 w-full items-center justify-between px-3 py-2 text-left text-sm text-(--color-text) transition-colors hover:bg-(--color-background-soft)"
                @click="selectCategory(category)"
              >
                <span>{{ category.categoryName }}</span>
                <span
                  v-if="category.categoryId === selectedCategoryId"
                  class="text-xs text-(--color-primary)"
                >
                  선택됨
                </span>
              </button>
            </li>
          </ul>
        </div>
        <button
          type="button"
          aria-label="카테고리 추가"
          class="inline-flex h-11 shrink-0 items-center justify-center gap-1.5 rounded-lg border border-(--color-border) bg-(--color-surface) px-3 text-sm font-medium text-(--color-text) transition-colors hover:cursor-pointer hover:border-(--color-primary) hover:text-(--color-primary)"
          @click="openCategoryDialog"
        >
          <Plus class="h-4 w-4" />
          <span class="hidden sm:inline">추가</span>
        </button>
      </div>
      <div
        v-if="categoryLoadError"
        class="mt-2 flex items-center gap-3 pl-19 text-sm text-(--color-error)"
      >
        <span>카테고리를 불러오지 못했습니다.</span>
        <button type="button" class="font-medium underline hover:cursor-pointer" @click="loadCategories">
          다시 시도
        </button>
      </div>
      <p
        v-else-if="!isLoadingCategories && categories.length === 0"
        class="mt-2 pl-19 text-sm text-(--color-text-secondary)"
      >
        등록된 카테고리가 없습니다. 오른쪽 추가 버튼으로 카테고리를 만들어주세요.
      </p>
    </section>

    <!-- 제목 입력 -->
    <section>
      <div class="flex items-center gap-3">
        <label for="post-title" class="w-16 shrink-0 text-sm font-semibold text-(--color-heading)">
          제목
        </label>
        <input
          id="post-title"
          v-model="title"
          type="text"
          maxlength="200"
          placeholder="제목을 입력하세요"
          class="h-11 w-full rounded-lg border border-(--color-border) bg-(--color-surface) px-4 text-sm text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-muted) focus:border-(--color-primary)"
        />
      </div>
    </section>

    <!-- 태그 선택 -->
    <section>
      <div class="flex items-center gap-3">
        <h2 class="w-16 shrink-0 text-sm font-semibold text-(--color-heading)">태그</h2>
        <div ref="tagPicker" class="relative min-w-0 flex-1" @focusout="closeTagListOnFocusout">
          <label class="relative block">
            <span class="sr-only">태그 검색</span>
            <Search
              class="pointer-events-none absolute top-1/2 left-3 h-4 w-4 -translate-y-1/2 text-(--color-text-secondary)"
            />
            <input
              v-model="tagSearch"
              type="search"
              placeholder="태그 검색"
              aria-label="태그 검색"
              :aria-expanded="isTagListOpen"
              aria-controls="available-tag-list"
              role="combobox"
              aria-autocomplete="list"
              class="h-11 w-full rounded-lg border border-(--color-border) bg-(--color-surface) pr-3 pl-10 text-sm text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-muted) focus:border-(--color-primary)"
              @focus="isTagListOpen = true"
              @keydown.esc="handleEsc"
            />
          </label>
          <ul
            v-if="isTagListOpen"
            id="available-tag-list"
            role="listbox"
            aria-label="태그 목록"
            class="my-scroll absolute top-full z-20 mt-1 max-h-56 w-full overflow-y-auto rounded-lg border border-(--color-border) bg-(--color-surface-elevated) py-1 shadow-md"
          >
            <li v-for="tag in filteredTags" :key="tag.tagName">
              <button
                type="button"
                role="option"
                :aria-selected="selectedTags.some((t) => t.tagName === tag.tagName)"
                class="flex min-h-11 w-full items-center justify-between px-3 py-2 text-left text-sm text-(--color-text) transition-colors hover:bg-(--color-background-soft)"
                @click="toggleTag(tag)"
              >
                <span>#{{ tag.tagName }}</span>
                <span
                  v-if="selectedTags.some((t) => t.tagName === tag.tagName)"
                  class="text-xs text-(--color-primary)"
                >
                  선택됨
                </span>
              </button>
            </li>
            <li v-if="filteredTags.length === 0">
              <button
                type="button"
                class="flex min-h-11 w-full items-center gap-2 px-3 py-2 text-left text-sm text-(--color-heading) transition-colors hover:bg-(--color-background-soft)"
                @click="openTagDialog"
              >
                <Plus class="h-4 w-4" />
                '{{ tagSearch.trim() }}' 태그 추가하기
              </button>
            </li>
          </ul>
        </div>
      </div>

      <div class="mt-3 flex min-h-9 items-center gap-3">
        <span class="w-16 shrink-0 text-xs text-(--color-text-secondary)">
          {{ selectedTags.length ? '선택됨' : '' }}
        </span>
        <div class="flex min-w-0 flex-1 flex-wrap items-center gap-2">
          <button
            v-for="tag in selectedTags"
            :key="tag.tagName"
            type="button"
            aria-label="선택한 태그 해제"
            class="min-h-9 rounded-lg border border-(--color-border) bg-(--color-background-soft) px-3 py-1 text-xs text-(--color-text) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
            @click="toggleTag(tag)"
          >
            #{{ tag.tagName }} <span aria-hidden="true">×</span>
          </button>
          <p v-if="!selectedTags.length" class="text-xs text-(--color-text-secondary)">
            선택된 태그가 없습니다.
          </p>
        </div>
      </div>
    </section>

    <!-- 본문 입력 -->
    <PostContentEditor v-model="content" />

    <TagCreateDialog
      :open="isTagDialogOpen"
      :tag-name="newTagName"
      :is-submitting="isCreatingTag"
      @submit="addTag"
      @close="isTagDialogOpen = false"
    />

    <CategoryCreateDialog
      :open="isCategoryDialogOpen"
      :is-submitting="isCreatingCategory"
      @submit="addCategory"
      @close="isCategoryDialogOpen = false"
    />
  </section>
</template>
