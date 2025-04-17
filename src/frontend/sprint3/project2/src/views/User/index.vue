<template>
  <v-app id="inspire">
    <Sidebar v-model="drawer" />

    <v-main class="main-container">
      <!-- 使用 Header 组件，并通过 v-model 绑定 drawer 状态 -->
      <Header v-model:drawer="drawer" :transparent="isTransparentHeader" />

      
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed, ref } from 'vue'
import Header from './components/Header.vue'
import Sidebar from './components/Sidebar.vue'

const drawer = ref(true)
const route = useRoute()

const isTransparentHeader = computed(() => route.path === '/user/location')
</script>

<style>
.main-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  background-color: #f7f7f7;
}


</style>