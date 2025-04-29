<template>
  <div class="index-container">
    <!-- 引用 LayoutHeader -->
    <LayoutHeader :is-transparent="isHeaderTransparent" />

    <div class="section home-section" id="home">
      <div class="content">
        <h1 class="slogan">This is a simple slogan</h1> <!-- 标语 -->
        <!-- <button class="login-button" @click="goToLogin">Login to Use</button> -->
        <button 
          class="login-button" 
          @click="goToLogin" 
          v-if="!isLoggedIn" >
          Login to Use
        </button>
        <button 
          class="gotodash-button" 
          @click="goToDashboard" 
          v-if="isLoggedIn" >
          Go to Dashboard
        </button>
      </div>
    </div>

    <div class="section vehicles-section" id="vehicles">
      <h2>Vehicles</h2>
      <div class="card-container">
        <div class="card">
          <h3>Card 1</h3>
          <p>This is the content of card 1.</p>
        </div>
        <div class="card">
          <h3>Card 2</h3>
          <p>This is the content of card 2.</p>
        </div>
        <div class="card">
          <h3>Card 3</h3>
          <p>This is the content of card 3.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'; // 引入 Vuex
import LayoutHeader from '@/views/Layout/components/LayoutHeader.vue';

const router = useRouter();
const store = useStore(); // 使用 Vuex
const isLoggedIn = computed(() => store.getters.isLoggedIn); // 获取登录状态
console.log('Is logged in:', isLoggedIn.value); // 登陆后获取登录状态
const isHeaderTransparent = ref(true); // 控制 Header 是否为透明背景



// 跳转到登录页面
const goToLogin = () => {
  router.push('/login');
};

// 监听滚动事件
const handleScroll = () => {
  const homeSection = document.getElementById('home');
  const homeSectionBottom = homeSection.offsetTop + homeSection.offsetHeight;

  if (window.scrollY < homeSectionBottom) {
    isHeaderTransparent.value = true; // 在第一个 div 时 Header 透明
  } else {
    isHeaderTransparent.value = false; // 在其他 div 时 Header 白底黑字
  }
};

// 添加滚动监听
onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

// 移除滚动监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped lang="scss">
.index-container {
  display: flex;
  flex-direction: column;
}

.section {
  height: 800px; /* 固定高度为 800px */
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.home-section {
  background: url('@/assets/images/home.png') no-repeat center / cover;
  justify-content: flex-end; /* 标语和登录按钮下移 */
  padding-bottom: 200px; /* 增加底部间距 */

  .content {
    text-align: center;

    .slogan {
      font-size: 48px;
      font-weight: bold;
      color: #fff;
      margin-bottom: 20px;
    }

    .login-button {
      background: #ffcc00; /* 黄底 */
      color: #000; /* 黑字 */
      border: none;
      padding: 15px 30px;
      font-size: 18px;
      border-radius: 8px;
      cursor: pointer;
      transition: background 0.3s ease, color 0.3s ease;

      &:hover {
        background: #000; /* 黑底 */
        color: #fff; /* 白字 */
      }
    }
  }
}

.vehicles-section,
.how-to-use-section,
.why-section,
.about-us-section {
  h2 {
    font-size: 36px;
    margin-bottom: 40px;
    margin-top: 60px; /* 标题与卡片之间的间距 */
  }

  .card-container {
    display: flex;
    justify-content: space-between;
    width: 100%; 
    height: 50%; 
    max-width: 1200px;

    .card {
      background: #f5f5f5;
      border-radius: 12px;
      padding: 20px;
      width: 40%; /* 卡片宽度变大 */
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s ease;
      margin-left: 30px;

      &:hover {
        transform: translateY(-10px);
      }

      h3 {
        font-size: 24px;
        margin-bottom: 10px;
      }

      p {
        font-size: 16px;
        color: #666;
      }
    }

    .yellow-card {
      background: #ffcc00; 
      color: #000;

      p {
        color: #000; 
      }
    }
  }
}

.vehicles-section {
  background: #fff;
  color: #000;
}

.how-to-use-section {
  background: #000;
  color: #fff;
}

.why-section {
  background: #1e1e1e;
  color: #fff;
}

.about-us-section {
  background: #fff;
  color: #000;
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

    &:hover {
      background: #3800bb;
    }
  }
</style>