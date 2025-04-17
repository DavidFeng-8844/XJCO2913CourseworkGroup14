<template>
  <v-app-bar app absolute color="white" height="64" class="custom-app-bar" elevation="0">
    <div class="header-inner" :class="{ transparent: transparent }">
      <v-app-bar-nav-icon @click="toggleDrawer" />

      <v-spacer />

      <div class="user-info">
        <div class="username">5ubmerge</div>
        <div class="email" :class="{ transparent: transparent }">18884730609@163.com</div>
      </div>

      <v-avatar class="avatar ml-3" size="36">
        <img src="@/assets/images/avatar.png" alt="avatar" />
      </v-avatar>
    </div>
  </v-app-bar>
</template>


<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  drawer: Boolean,
  customClass: String,
  transparent: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(['update:drawer']);

const toggleDrawer = () => {
  emit('update:drawer', !props.drawer);
};
</script>

<style scoped>
.custom-app-bar {
  background-color: transparent !important;
  box-shadow: none;
  margin-top: 15px;
}

.header-inner {
  width: 96%;
  max-width: 1280px;
  margin: 8px auto;
  /* 上下间距，让卡片浮起来 */
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  background-color: white;
  /* ✅ 白色背景 */
  border-radius: 12px;
  /* ✅ 圆角 */
  border: 1px solid #e0e0e0;
  /* ✅ 灰色细边框 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
  /* ✅ 微阴影，选配 */
}

.header-inner.transparent {
  background-color: transparent;
  border: none;
  box-shadow: none;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-right: 10px;
}

.username {
  font-weight: bold;
  font-size: 14px;
}

.email {
  font-size: 12px;
  color: #757575;
}

.email.transparent{
  display: none;
}

.v-avatar {
  border: 2px solid #ddd;
}


.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保证图片完整显示 */
  border-radius: 50%;
}
</style>