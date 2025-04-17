import { createRouter, createWebHistory } from 'vue-router';
import Layout from '@/views/Layout/index.vue';
import Login from '@/views/Login/index.vue';
import Test from '@/views/Test.vue' 
import UserCenter from '@/views/User/index.vue'; // 用户中心主页面
import Wallet from '@/views/User/Wallet.vue'; // 我的钱包
import Store from '@/views/User/Store.vue'; // 套餐商店
import Orders from '@/views/User/Orders.vue'; // 查看订单
import Location from '@/views/User/Location.vue'; // 查找位置
import Information from '@/views/User/Information.vue'

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
      path: '/user',
      component: UserCenter,
      children: [
        {
          path: 'wallet',
          component: Wallet,
        },
        {
          path: 'store',
          component: Store,
        },
        {
          path: 'orders',
          component: Orders,
        },
        {
          path: 'location',
          component: Location,
        },
        {
          path: 'information',   
          component: Information
        }
      ],
    },
    {
      path: '/test',
      name: 'Test',
      component: Test
    }
  ],
});

export default router;