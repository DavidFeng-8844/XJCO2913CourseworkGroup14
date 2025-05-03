import { createRouter, createWebHistory } from 'vue-router';
import Layout from '@/views/Layout/index.vue';
import Login from '@/views/Login/index.vue';
import Dashboard from '@/views/Dashboard/index.vue'; // 仪表盘
import AddScooter from '@/views/Scooter/index.vue'; // 添加电动车
import viewData from '@/views/viewData/index.vue'; // 数据展示

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Layout,
    },
    {
      path: '/login',
      component: Login,
    },
    {
      path: '/dashboard',
      component: Dashboard,
    },
    {
      path: "/add-scooter",
      component: AddScooter,
    },
    {
      path: '/data',
      component: viewData,
    },
  ],
});

export default router;