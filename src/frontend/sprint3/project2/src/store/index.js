// stores/index.js
import { createStore } from 'vuex';
import { loginAPI } from '@/apis/user'; // 确保正确引入你的 loginAPI


const store = createStore({
  state: {
    // isLoggedIn: false,
    // user: null,
    user: JSON.parse(localStorage.getItem('user')) || null, // 从 localStorage 初始化用户信息
    isLoggedIn: !!localStorage.getItem('user'), // Boolean值，是否已登录
    isAdmin: (state) => state.user?.roles.includes('ROLE_ADMIN') 
  },
  mutations: {
    login(state, user) {
      console.log('User logged in:', user);
      state.isLoggedIn = true;
      // state.user = user; // 存储用户信息
      state.user = {
        username: user.username, // 确保获取的 username 字段
        email: user.email,       // 确保获取的 email 字段
      };
      console.log('User information stored in Vuex:', state.user);
      localStorage.setItem('user', JSON.stringify(user)); // 存储用户信息
    },
    logout(state) {
      state.isLoggedIn = false;
      state.user = null; // 清空用户信息
      localStorage.removeItem('user'); // 清除用户信息
    },
  },
  actions: {
    async login({ commit }, { username, password }) {
      let isLoginActionTriggered = false;
      if (isLoginActionTriggered) return; // 防止重复调用
      console.log('Login action called with:', { username, password });
      try {
        const res = await loginAPI({ username, password }); // 调用登录 API
        console.log('API Response in Vuex:', res);
        // 检查 API 返回的状态是否成功
        if (res && res.message && res.user && typeof res === 'object') {
          commit('login', res.user); // 提交用户信息
          console.log('User from API response:', res.user);
          return res; // 成功后返回结果
        } else {
          throw new Error('Invalid response format'); // 抛出错误以供捕捉
        }
      } catch (error) {
        console.error("Login action error:", error); // 记录错误
        throw error; // 让调用者处理错误
      }
    },
    logout({ commit }) {
      commit('logout');
    },
  },
  getters: {
    isLoggedIn: state => state.isLoggedIn,
    user: state => state.user, // 获取用户信息
    isAdmin: state => state.user && state.user.roles && state.user.roles.includes('ROLE_ADMIN'), // 判断是否为管理员
  },
});

export default store;