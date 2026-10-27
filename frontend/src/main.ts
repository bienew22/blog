import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { setUnauthorizedHandler } from './api/client'
import { useAuthStore } from './stores/auth'

const app = createApp(App)

app.use(createPinia())
app.use(router)

setUnauthorizedHandler(() => {
  useAuthStore().logout()
  void router.push({
    name: 'login',
    query: { redirect: router.currentRoute.value.fullPath },
  })
})
app.mount('#app')
