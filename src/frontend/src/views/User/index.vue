<template>
  <v-app id="inspire">
    <Sidebar v-model="drawer" />

    <v-main class="main-container">
      <!-- 使用 Header 组件，并通过 v-model 绑定 drawer 状态 -->
      <Header v-model:drawer="drawer" />
      
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue';
import Sidebar from './components/Sidebar.vue';
import Header from './components/Header.vue'; // 引入 Header 组件

const drawer = ref(true); // 控制侧边栏的显示和隐藏
</script>

<style>

</style>