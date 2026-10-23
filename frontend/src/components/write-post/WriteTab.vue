<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import PostContentEditor from '@/components/write-post/PostContentEditor.vue'
import { Plus, Search } from 'lucide-vue-next'
import { fetchTags, createTag, type Tag } from '@/api/tags'

const title = defineModel<string>('title', { required: true })
const content = defineModel<string>('content', { required: true })
const selectedTags = defineModel<Tag[]>('selectedTags', { required: true })

const emit = defineEmits<{
  toast: [message: string, type: 'pending' | 'success' | 'error', duration: number]
}>()

const availableTags = ref<Tag[]>([])
const tagSearch = ref('')
const isTagListOpen = ref(false)
const tagPicker = ref<HTMLElement | null>(null)
const isCreatingTag = ref(false) // 생성 완료전까지 중복 클릭 예방용

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

async function addTag(rawName: string) {
  const tagName = rawName.trim()
  if (!tagName || isCreatingTag.value) return

  isTagListOpen.value = false
  isCreatingTag.value = true
  emit('toast', '태그 생성 중...', 'pending', 2000)

  try {
    const created = await createTag(tagName)

    availableTags.value.push(created)
    selectedTags.value.push(created)
    tagSearch.value = ''

    emit('toast', '태그 생성 완료', 'success', 2000)
  } catch (e) {
    emit('toast', e instanceof Error ? e.message : '태그 생성에 실패했습니다.', 'error', 2000)
  } finally {
    isCreatingTag.value = false
  }
}
</script>

<template>
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
        class="h-12 w-full rounded-lg border border-(--color-border) bg-(--color-surface) px-4 text-base text-(--color-text) outline-none transition-colors placeholder:text-(--color-text-muted) focus:border-(--color-primary)"
      />
    </section>

    <section>
      <div class="flex items-center gap-3">
        <h2 class="shrink-0 text-sm font-semibold text-(--color-heading)">태그</h2>
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
                @click="addTag(tagSearch)"
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
          :key="tag.tagName"
          type="button"
          aria-label="선택한 태그 해제"
          class="min-h-9 rounded-lg border border-(--color-border) bg-(--color-background-soft) px-3 py-1 text-xs text-(--color-text) transition-colors hover:border-(--color-primary) hover:text-(--color-primary)"
          @click="toggleTag(tag)"
        >
          #{{ tag.tagName }} <span aria-hidden="true">×</span>
        </button>
      </div>
      <p v-else class="mt-3 text-sm text-(--color-text-secondary)">선택된 태그가 없습니다.</p>
    </section>

    <PostContentEditor v-model="content" />
  </section>
</template>
