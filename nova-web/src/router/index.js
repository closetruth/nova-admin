import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/index.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/index',
    children: [
      { path: 'index', name: 'home', component: () => import('@/views/home/index.vue'), meta: { title: '首页' } },
      { path: 'dept', name: 'dept', component: () => import('@/views/dept/index.vue'), meta: { title: '部门管理' } },
      { path: 'emp', name: 'emp', component: () => import('@/views/emp/index.vue'), meta: { title: '员工管理' } },
      { path: 'clazz', name: 'clazz', component: () => import('@/views/clazz/index.vue'), meta: { title: '班级管理' } },
      { path: 'stu', name: 'stu', component: () => import('@/views/stu/index.vue'), meta: { title: '学员管理' } },
      { path: 'report/emp', name: 'reportEmp', component: () => import('@/views/report/emp.vue'), meta: { title: '员工信息统计' } },
      { path: 'report/stu', name: 'reportStu', component: () => import('@/views/report/stu.vue'), meta: { title: '学员信息统计' } },
      { path: 'log', name: 'log', component: () => import('@/views/log/index.vue'), meta: { title: '操作日志' } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  if (!to.meta.public && !userStore.isLogin) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/login' && userStore.isLogin) {
    return { path: '/index' }
  }
  return true
})

export default router
