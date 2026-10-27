import { createRouter, createWebHistory } from 'vue-router'
import PostsView from '../views/PostsView.vue'
import HomeView from '../views/HomeView.vue'
import CategoriesView from '../views/CategoriesView.vue'
import TagsView from '../views/TagsView.vue'
import LoginView from '../views/LoginView.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition
    if (to.hash) return { el: to.hash, top: 80 }
    if (to.path === from.path) return false
    return { top: 0, left: 0, behavior: 'instant' }
  },
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/posts',
      name: 'posts',
      component: PostsView,
    },
    {
      path: '/categories',
      name: 'categories',
      component: CategoriesView,
    },
    {
      path: '/tags',
      name: 'tags',
      component: TagsView,
    },
    {
      path: '/tag/:tagName',
      name: 'tag',
      component: () => import('../views/TagPostsView.vue'),
    },
    {
      path: '/post/:id',
      name: 'post',
      component: () => import('../views/PostView.vue'),
    },
    {
      path: '/404',
      name: 'not-found',
      component: () => import('../views/NotFoundView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/write/:id',
      name: 'edit-post',
      component: () => import('../views/WritePostView.vue'),
      meta: { requiresAdmin: true },
    },
    {
      path: '/write',
      name: 'write',
      component: () => import('../views/WritePostView.vue'),
      meta: { requiresAdmin: true },
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: { name: 'not-found' },
    },
  ],
})

router.beforeEach((to, from) => {
  if (!to.meta.requiresAdmin) return
  const authStore = useAuthStore()
  if (!authStore.user) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  if (!authStore.isAdmin) {
    // 취소하면 이전 페이지에 머무름. 직접 진입한 경우에는 홈으로 이동
    return from.matched.length ? false : { name: 'home' }
  }
})

export default router
