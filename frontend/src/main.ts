import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { setRefreshHandler, setUnauthorizedHandler } from './api/client'
import { useAuthStore } from './stores/auth'

const app = createApp(App)

app.use(createPinia())

setRefreshHandler(() => useAuthStore().refresh())
setUnauthorizedHandler(() => {
  useAuthStore().logout()
  void router.push({
    name: 'login',
    query: { redirect: router.currentRoute.value.fullPath },
  })
})

// 새로고침 등으로 메모리의 access token이 사라져도 refresh 쿠키가 있으면 세션 복원 (라우터 가드 전에 수행)
void useAuthStore()
  .refresh()
  .catch(() => false)
  .finally(() => {
    app.use(router)
    app.mount('#app')
  })
