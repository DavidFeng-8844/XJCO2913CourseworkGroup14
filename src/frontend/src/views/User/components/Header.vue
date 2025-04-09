<template>
  <v-app-bar absolute :elevation="2" class="custom-app-bar" :class="[{'app-bar-collapsed': !drawer, 'app-bar-expanded': drawer}, customClass]">
    <template v-slot:prepend>
      <v-app-bar-nav-icon @click="toggleDrawer"></v-app-bar-nav-icon>
    </template>

    <v-app-bar-title>Title</v-app-bar-title>

    <template v-slot:append>
      <!-- 用户名和邮箱的 div -->
      <div class="user-info">
        <div class="username">用户名</div>
        <div class="email">18884730609@163.com</div>
      </div>

      <!-- Avatar -->
      <v-avatar class="ml-3">
        <v-img src="https://example.com/avatar.jpg" alt="User Avatar"></v-img>
      </v-avatar>
    </template>
  </v-app-bar>
</template>
  
  <script setup>
  import { defineProps, defineEmits } from 'vue';
  
  const props = defineProps({
    drawer: {
      type: Boolean,
      required: true,
    },
    customClass: {
      type: String,
      default: '',
    },
  });
  
  const emit = defineEmits(['update:drawer']);
  
  const toggleDrawer = () => {
    emit('update:drawer', !props.drawer);
  };
  </script>
  
  <style scoped>
  .custom-app-bar {
    margin: 20px;
    box-sizing: border-box !important;
    padding: 0 20px !important;
    border-radius: 12px !important;
    width: calc(100% - 40px) !important;
    transition: width 0.3s ease;
  }
  
  .app-bar-expanded {
    max-width: 82%;
  }
  
  .app-bar-collapsed {
    max-width: 95%;
    justify-content: center;
    margin-left: 3rem;
  }
  .user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-right: 10px;
}

.username {
  font-weight: bold; /* 加粗用户名 */
  font-size: 14px;
}

.email {
  font-size: 12px;
  color: #757575; /* 灰色邮箱 */
}

.v-avatar {
  border: 2px solid #ddd; /* 头像边框 */
}
  </style>