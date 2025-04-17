<template>
    <v-navigation-drawer v-model="drawerValue">
      <!-- Logo 和品牌名称 -->
      <v-sheet class="pa-4" color="grey-lighten-4">
        <div class="d-flex align-center">
          <!-- Logo -->
          <v-avatar class="mr-2" color="primary" size="40">
            <v-img src="https://example.com/logo.png" alt="Logo"></v-img>
          </v-avatar>
  
          <!-- 品牌名称 -->
          <div class="brand-name">Brand Name</div>
        </div>
      </v-sheet>
  
      <v-divider></v-divider>
  
      <v-list>
        <!-- 用户中心 -->
        <v-list-item
          prepend-icon="mdi-account-circle-outline"
          title="用户中心"
          value="/user/information"
          :to="{ path: '/user/information' }"
          exact
          active-class="active-item"
          class="sidebar-item"
          :ripple="false"
          :overlay="false"
        ></v-list-item>
  
        <!-- 其他选项 -->
        <v-list-item
          v-for="[icon, text, route] in links"
          :key="route"
          :prepend-icon="icon"
          :title="text"
          :value="route"
          :to="{ path: route }"
          active-class="active-item"
          class="sidebar-item"
          :ripple="false"
        ></v-list-item>
      </v-list>
    </v-navigation-drawer>
  </template>
  
  <script setup>
  import { computed } from 'vue';
  
  const props = defineProps({
    modelValue: Boolean, // 接收父组件传递的值
  });
  
  const emit = defineEmits(['update:modelValue']); // 定义事件，用于更新父组件的值
  
  const links = [
    ['mdi-wallet-outline', '我的钱包', '/user/wallet'],
    ['mdi-store-outline', '套餐商店', '/user/store'],
    ['mdi-clipboard-list-outline', '查看订单', '/user/orders'],
    ['mdi-map-marker-outline', '查找位置', '/user/location'],
  ];
  
  // 使用 computed 实现双向绑定
  const drawerValue = computed({
    get() {
      return props.modelValue;
    },
    set(value) {
      emit('update:modelValue', value);
    },
  });
  </script>
  
  <style scoped>
  /* Logo 和品牌名称的样式 */
  .brand-name {
    font-size: 20px;
    font-weight: bold;
    color: #333;
  }
  
  /* 选中时的样式 */
  .active-item {
    margin: 10px;
    background-color: #00dd00 !important; /* 选中时背景颜色 */
    border-radius: 8px !important; /* 圆角 */
  }
  
  /* 侧边栏选项的样式 */
  .v-list-item.sidebar-item {
    margin: 10px;
    transition: transform 0.3s ease, box-shadow 0.3s ease, border-radius 0.3s ease; /* 添加动画效果 */
    border-radius: 8px !important; /* 圆角 */
  }
  
/* ✅ 所有 hover 项都向右移动 */
.v-list-item.sidebar-item:hover {
  transform: translateX(10px);
  transition: transform 0.3s ease;
}

/* ✅ 非激活项才变白色背景 */
.v-list-item.sidebar-item:hover:not(.active-item) {
  background-color: #ffffff !important;
}

  
  /* 使用 :deep() 穿透作用域 */
  :deep(.v-list-item.sidebar-item .v-list-item__content .v-list-item-title) {
    font-size: 20px !important; /* 调整字体大小 */
  }
  
  :deep(.v-list-item.sidebar-item .v-list-item__overlay) {
    background-color: white !important; /* 设置覆盖层颜色为白色 */
  }
  
  .v-ripple_container {
    background-color: #ffffff;
  }
  </style>