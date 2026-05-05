import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/nhan-vien',
      name: 'Nhân Viên',
      component: () => import('../views/NhanVienView.vue'),
    },
    {
      path: '/nhan-vien/edit/:id',
      name: 'editNhanVien',
      component: () => import('../components/FormNhanVienComponents.vue')
    },
    {
      path: '/nhan-vien/add',
      name: 'addNhanVien',
      component: () => import('../components/FormNhanVienComponents.vue'),
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    },
  ],
})

export default router
