<template>
  <header class="app-header" :class="{ transparent: isTransparent }">
    <div class="container">
      <!-- 左侧 Logo 和品牌名 -->
      <div class="brand">
        <!-- link button-->
        <router-link to="/" class="brand-name">Scootify</router-link>
        <span class="slogan">Scooter Sharing System</span>
      </div>

      <!-- 右侧导航和登录按钮 -->
      <div class="nav-and-login">
        <!-- 添加条件渲染以显示或隐藏登录按钮和用户链接 -->
        <button v-if="!isLoggedIn" class="login-button" @click="goToLogin">Login</button> 
        <!-- <span v-if="isLoggedIn && user">{{ user.username }}</span> -->
        <!-- button got to dashboard with specific username -->
        <button v-if="isLoggedIn" class="gotodash-button" @click="goToDashboard">User: {{ user.username }}</button>
        <button v-if="isLoggedIn" class="logout-button" @click="logout">Logout</button>
        <!-- Admin -->
        <button v-if="isAdmin" class="admin-button" @click="data">Data</button>
        <button v-if="isAdmin" class="admin-button" @click="scooter">Scooter</button>
        
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

const isAdmin = computed(() => store.getters.isAdmin);
console.log('Is admin:', isAdmin.value); // 获取管理员状态

// 登出功能
const logout = () => {
  store.dispatch('logout'); // 调用 Vuex 中的登出 action
  router.push('/login'); // 登出后重定向到主页
};

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login');
};

// 跳转到 Dashboard 页面
const goToDashboard = () => {
  router.push('/dashboard');
};

// 跳转到数据页面
const data = () => {
  router.push('/data');
};

// 跳转到滑板车页面
const scooter = () => {
  router.push('/add-scooter');
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


  .gotodash-button {
  background: #0593b6;
  color: #fff;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
  margin-right: 10px; /* Add space between buttons */

  &:hover {
    background: #3800bb;
  }
}

.logout-button {
  background: #0004ff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;

  &:hover {
    background: #bb0000;
  }
}

.admin-button {
  background: #ffcc00;
  color: #000;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  margin-inline: 10px;
  transition: background 0.3s ease;
  &:hover {
    background: #bbbb00;
  }
}
}
</style>