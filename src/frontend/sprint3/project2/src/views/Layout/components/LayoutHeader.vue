<template>
  <header class="app-header" :class="{ transparent: isTransparent }">
    <div class="container">
      <!-- 左侧 Logo 和品牌名 -->
      <div class="brand">
        <span class="brand-name">Brand Name</span>
      </div>

      <!-- 右侧导航和登录按钮 -->
      <div class="nav-and-login">
        <ul class="app-header-nav">
          <li><a href="#vehicles">Vehicles</a></li>
          <li><a href="#how-to-use">How</a></li>
          <li><a href="#why">Why</a></li>
          <li><a href="#about-us">About Us</a></li>
        </ul>

        <!-- 登录按钮 -->
        <!-- <button class="login-button" @click="goToLogin">Login</button> -->
        <!-- 添加条件渲染以显示或隐藏登录按钮和用户链接 -->
        <button v-if="!isLoggedIn" class="login-button" @click="goToLogin">Login</button> 
        <span v-if="isLoggedIn && user">{{ user.username }}</span> <!-- 显示用户名 -->
        <p v-if="isLoggedIn">User is logged in</p>
        <p v-else>User is not logged in.</p>
        <p>User data: {{ user }}</p> <!-- 显示用户数据 -->
        <button v-if="isLoggedIn" @click="logout">Logout</button> <!-- 登出按钮 -->
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { computed, ref, onMounted } from 'vue';
import { useStore } from 'vuex'; // 引入 Vuex

const router = useRouter();
const store = useStore(); // 使用 Vuex

const props = defineProps({
  isTransparent: Boolean, // 接收父组件传递的透明度状态
});

// 获取登录状态和用户信息
const isLoggedIn = computed(() => store.getters.isLoggedIn);
const user = computed(() => store.getters.user || {}); // 获取用户信息，默认为空对象
console.log('User in component:', user.value); // 登陆后获取 user 信息
console.log('Is logged in:', isLoggedIn.value); // 登陆后获取登录状态

// const isLoading = ref(true);

// onMounted(async() => {
//     if (!isLoggedIn.value) {
//         await store.dispatch('fetchUserData').finally(() => {
//           console.log('Current user state after fetching:', store.getters.user);
//           isLoading.value = false;
//         });
//     } else {
//         isLoading.value = false;
//     }
// });

// 登出功能
const logout = () => {
  store.dispatch('logout'); // 调用 Vuex 中的登出 action
  router.push('/login'); // 登出后重定向到主页
};

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped lang="scss">
.app-header {
  background: #fff;
  padding: 10px 0;
  border-bottom: 1px solid #e7e7e7;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  transition: background 0.3s ease, color 0.3s ease;

  &.transparent {
    background: transparent;
    border-bottom: none;

    .brand-name,
    .app-header-nav a,
    .login-button,
    .slogan {
      color: #fff !important; /* 提高优先级 */
    }
  }

  .container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .brand {
    display: flex;
    align-items: center;
    margin-left: -20px; /* 标题框靠左一点 */

    .brand-name {
      font-size: 20px;
      font-weight: bold;
      color: #000;
      transition: color 0.3s ease;
    }

    .slogan {
      font-size: 16px;
      font-weight: bold;
      color: #000;
      margin-left: 20px; /* 标语与品牌名的间距 */
      transition: color 0.3s ease;
    }
  }

  .nav-and-login {
    display: flex;
    align-items: center;
  }

  .app-header-nav {
    display: flex;
    list-style: none;
    margin: 0;
    padding: 0;

    li {
      margin-right: 20px;

      a {
        text-decoration: none;
        color: #000;
        font-size: 16px;
        transition: color 0.3s ease;

        &:hover {
          color: #00dd00;
        }
      }
    }
  }

  .login-button {
    background: #00dd00;
    color: #fff;
    border: none;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.3s ease;

    &:hover {
      background: #00bb00;
    }
  }
}
</style>