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
      path: '/khach-hang',
      name: 'Khách Hàng',
      component: () => import('../views/KhachHangView.vue'),
    },
    {
      path: '/khach-hang/edit/:id',
      name: 'editKhachHang',
      component: () => import('../components/FormKhachHangComponents.vue')
    },
    {
      path: '/khach-hang/add',
      name: 'addKhachHang',
      component: () => import('../components/FormKhachHangComponents.vue'),
    },
    {
      path: '/ca-lam-viec',
      name: 'Ca Làm  Viêc',
      component: () => import('../views/CaLamViecView.vue'),
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    },
  ],
})

export default router
