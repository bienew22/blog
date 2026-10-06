<script setup lang="ts">
import { computed, ref } from 'vue'
import BlogLayout from '@/components/layout/BlogLayout.vue'
import { Eye, Plus, Save, Search, Send, X } from 'lucide-vue-next'
import { trendingTags } from '@/data/posts'

const title = ref('')
const content = ref('')
const availableTags = ref([...trendingTags])
const selectedTags = ref<string[]>([])
const isPreview = ref(false)
const tagSearch = ref('')
const isTagListOpen = ref(false)
const isTagDialogOpen = ref(false)
const newTagName = ref('')
const tagPicker = ref<HTMLElement | null>(null)

const filteredTags = computed(() => {
  const query = tagSearch.value.trim().toLocaleLowerCase()
  if (!query) return availableTags.value

  return availableTags.value.filter((tag) => tag.toLocaleLowerCase().includes(query))
})

function toggleTag(tagName: string) {
  selectedTags.value = selectedTags.value.includes(tagName)
    ? selectedTags.value.filter((tag) => tag !== tagName)
    : [...selectedTags.value, tagName]
  tagSearch.value = ''
  isTagListOpen.value = false
}

function closeTagListOnFocusout(event: FocusEvent) {
  const nextTarget = event.relatedTarget
  if (!(nextTarget instanceof Node) || !tagPicker.value?.contains(nextTarget)) {
    isTagListOpen.value = false
  }
}

function openTagDialog(tagName = '') {
  newTagName.value = tagName
  isTagListOpen.value = false
  isTagDialogOpen.value = true
}

function createTag() {
  const tagName = newTagName.value.trim()
  if (!tagName) return

  const existingTag = availableTags.value.find(
    (tag) => tag.toLocaleLowerCase() === tagName.toLocaleLowerCase(),
  )
  const tagToSelect = existingTag ?? tagName

  if (!existingTag) availableTags.value.push(tagName)
  if (!selectedTags.value.includes(tagToSelect)) selectedTags.value.push(tagToSelect)

  tagSearch.value = ''
  isTagListOpen.value = false
  isTagDialogOpen.value = false
}
</script>

<template>
  <BlogLayout>
    <form class="mx-auto w-full max-w-4xl" @submit.prevent>
      <div class="mb-8 flex flex-wrap items-center justify-end gap-2">
        <button
          type="button"
          class="inline-flex h-10 items-center gap-2 rounded-md border border-(--color-border) px-4 text-sm font-medium text-(--color-heading) transition-colors hover:bg-(--color-background-soft) dark:border-(--color-border-hover) dark:hover:bg-(--color-background-mute)"
          :aria-pressed="isPreview"
          @click="isPreview = !isPreview"
        >
          <Eye class="h-4 w-4" />
          {{ isPreview ? '작성하기' : '미리보기' }}
        </button>
        <button
          type="button"
          class="inline-flex h-10 items-center gap-2 rounded-md border border-(--color-border) px-4 text-sm font-medium text-(--color-heading) transition-colors hover:bg-(--color-background-soft) dark:border-(--color-border-hover) dark:hover:bg-(--color-background-mute)"
        >
          <Save class="h-4 w-4" />
          임시저장
        </button>
        <button
          type="button"
          class="inline-flex h-10 items-center gap-2 rounded-md bg-(--color-heading) px-4 text-sm font-medium text-(--color-background) transition-opacity hover:opacity-85"
        >
          <Send class="h-4 w-4" />
          저장
        </button>
      </div>

      <section class="space-y-8">
        <section>
          <label class="mb-3 block text-sm font-semibold text-(--color-heading)" for="post-title">
            제목
          </label>
          <input
            id="post-title"
            v-model="title"
            type="text"
            maxlength="200"
            placeholder="제목을 입력하세요"
            class="h-12 w-full rounded-md border border-(--color-border) bg-(--color-background) px-4 text-base text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-secondary) focus:border-(--color-hover-title) dark:border-(--color-border-hover) dark:bg-[#29292f]"
          />
        </section>

        <section>
          <div class="flex items-center gap-3">
            <h2 class="shrink-0 text-sm font-semibold text-(--color-heading)">태그</h2>
            <div
              ref="tagPicker"
              class="relative min-w-0 flex-1"
              @focusout="closeTagListOnFocusout"
            >
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
                  class="h-11 w-full rounded-md border border-(--color-border) bg-(--color-background) pr-3 pl-10 text-sm text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-secondary) focus:border-(--color-hover-title) dark:border-(--color-border-hover) dark:bg-[#29292f]"
                  @focus="isTagListOpen = true"
                  @keydown.esc="isTagListOpen = false"
                />
              </label>
              <ul
                v-if="isTagListOpen"
                id="available-tag-list"
                role="listbox"
                aria-label="태그 목록"
                class="absolute top-full z-20 mt-1 max-h-56 w-full overflow-y-auto rounded-md border border-(--color-border-hover) bg-(--color-background) py-1 shadow-lg dark:bg-[#29292f]"
              >
                <li v-for="tag in filteredTags" :key="tag">
                  <button
                    type="button"
                    role="option"
                    :aria-selected="selectedTags.includes(tag)"
                    class="flex w-full items-center justify-between px-3 py-2 text-left text-sm text-(--color-text) transition-colors hover:bg-(--color-background-soft) dark:hover:bg-(--color-background-mute)"
                    @click="toggleTag(tag)"
                  >
                    <span>#{{ tag }}</span>
                    <span v-if="selectedTags.includes(tag)" class="text-xs text-(--color-hover-title)">
                      선택됨
                    </span>
                  </button>
                </li>
                <li v-if="filteredTags.length === 0">
                  <button
                    type="button"
                    class="flex w-full items-center gap-2 px-3 py-2 text-left text-sm text-(--color-heading) transition-colors hover:bg-(--color-background-soft) dark:hover:bg-(--color-background-mute)"
                    @click="openTagDialog(tagSearch.trim())"
                  >
                    <Plus class="h-4 w-4" />
                    '{{ tagSearch.trim() }}' 태그 추가하기
                  </button>
                </li>
              </ul>
            </div>
          </div>

          <div v-if="selectedTags.length" class="mt-4 flex flex-wrap items-center gap-2">
            <span class="mr-1 text-xs text-(--color-text-secondary)">선택됨</span>
            <button
              v-for="tag in selectedTags"
              :key="tag"
              type="button"
              aria-label="선택한 태그 해제"
              class="rounded-md border border-(--color-border) bg-(--color-background-soft) px-2.5 py-1 text-xs text-(--color-heading) transition-colors hover:border-(--color-border-hover) dark:border-(--color-border-hover) dark:bg-(--color-background-mute)"
              @click="toggleTag(tag)"
            >
              #{{ tag }} <span aria-hidden="true">×</span>
            </button>
          </div>
          <p v-else class="mt-3 text-sm text-(--color-text-secondary)">선택된 태그가 없습니다.</p>
        </section>

        <section>
          <div class="mb-3 flex items-center justify-between">
            <h2 class="text-sm font-semibold text-(--color-heading)">본문</h2>
            <span class="text-xs text-(--color-text-secondary)">{{ content.length }}자</span>
          </div>
          <textarea
            v-if="!isPreview"
            v-model="content"
            placeholder="본문을 작성하세요"
            class="min-h-[55vh] w-full resize-y rounded-md border border-(--color-border) bg-(--color-background) p-4 leading-7 text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-secondary) focus:border-(--color-hover-title) dark:border-(--color-border-hover) dark:bg-[#29292f]"
          ></textarea>
          <div
            v-else
            class="min-h-[55vh] whitespace-pre-wrap rounded-md border border-(--color-border) bg-(--color-background-soft) p-4 leading-7 text-(--color-text) dark:border-(--color-border-hover) dark:bg-[#29292f]"
          >
            <h1 v-if="title" class="mb-4 text-2xl font-bold text-(--color-heading)">{{ title }}</h1>
            <div v-if="selectedTags.length" class="mb-4 flex flex-wrap gap-2">
              <span
                v-for="tag in selectedTags"
                :key="tag"
                class="rounded-md border border-(--color-border) px-2.5 py-1 text-xs text-(--color-text-secondary)"
              >#{{ tag }}</span>
            </div>
            <p v-if="content">{{ content }}</p>
            <p v-else class="text-(--color-text-secondary)">본문 미리보기가 여기에 표시됩니다.</p>
          </div>
        </section>
      </section>
    </form>

    <div
      v-if="isTagDialogOpen"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/55 p-4"
      @click.self="isTagDialogOpen = false"
      @keydown.esc="isTagDialogOpen = false"
    >
      <form
        role="dialog"
        aria-modal="true"
        aria-labelledby="create-tag-title"
        class="w-full max-w-md rounded-lg border border-(--color-border) bg-(--color-background) p-5 text-(--color-text) shadow-xl dark:border-(--color-border-hover)"
        @submit.prevent="createTag"
      >
        <div class="mb-5 flex items-center justify-between">
          <h2 id="create-tag-title" class="text-lg font-semibold text-(--color-heading)">
            태그 추가
          </h2>
          <button
            type="button"
            aria-label="태그 생성 창 닫기"
            class="rounded p-1 text-(--color-text-secondary) transition-colors hover:bg-(--color-background-soft) hover:text-(--color-heading)"
            @click="isTagDialogOpen = false"
          >
            <X class="h-5 w-5" />
          </button>
        </div>
        <label class="mb-5 block">
          <span class="mb-2 block text-sm font-medium text-(--color-heading)">태그 이름</span>
          <input
            v-model="newTagName"
            autofocus
            maxlength="40"
            type="text"
            placeholder="새 태그 이름"
            class="h-11 w-full rounded-md border border-(--color-border) bg-(--color-background) px-3 text-sm text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-secondary) focus:border-(--color-hover-title) dark:border-(--color-border-hover) dark:bg-[#29292f]"
          />
        </label>
        <div class="flex justify-end gap-2">
          <button
            type="button"
            class="h-10 rounded-md border border-(--color-border) px-4 text-sm text-(--color-heading) transition-colors hover:bg-(--color-background-soft) dark:border-(--color-border-hover)"
            @click="isTagDialogOpen = false"
          >
            취소
          </button>
          <button
            type="submit"
            :disabled="!newTagName.trim()"
            class="h-10 rounded-md bg-(--color-heading) px-4 text-sm font-medium text-(--color-background) transition-opacity hover:opacity-85 disabled:cursor-not-allowed disabled:opacity-50"
          >
            태그 추가
          </button>
        </div>
      </form>
    </div>
  </BlogLayout>
</template>