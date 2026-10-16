import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

const STORAGE_KEY = 'theme'

function getInitialIsDark(): boolean {
    return document.documentElement.classList.contains('dark')
}

export const useThemeStore = defineStore('theme', () => {
    const isDark = ref(getInitialIsDark())

    function toggleTheme() {
        isDark.value = !isDark.value
    }

    watch(isDark, (value) => {
        document.documentElement.classList.toggle('dark', value)
        localStorage.setItem(STORAGE_KEY, value ? 'dark' : 'light')
    })

    return { isDark, toggleTheme }
})
