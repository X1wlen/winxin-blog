import { createRouter, createWebHistory } from 'vue-router'

import BasicLayout from '@/components/layout/BasicLayout.vue'

declare module 'vue-router' {
  interface RouteMeta {
    /** 页面标题（afterEach 中同步到 document.title） */
    title?: string
  }
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: BasicLayout,
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('@/views/HomeView.vue'),
          meta: { title: '首页' },
        },
        {
          path: 'health',
          name: 'health',
          component: () => import('@/views/HealthView.vue'),
          meta: { title: '健康检查' },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFoundView.vue'),
      meta: { title: '404' },
    },
  ],
})

router.afterEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} · WinXin Blog` : 'WinXin Blog'
})

export default router
