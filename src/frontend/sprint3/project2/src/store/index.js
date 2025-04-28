// stores/index.js
import { createStore } from 'vuex';
import { loginAPI } from '@/apis/user'; // 确保正确引入你的 loginAPI

const store = createStore({
  state: {
    isLoggedIn: false,
    user: null,
  },
  mutations: {
    login(state, user) {
      state.isLoggedIn = true;
      state.user = user; // 存储用户信息
    },
    logout(state) {
      state.isLoggedIn = false;
      state.user = null; // 清空用户信息
    },
  },
  actions: {
    async login({ commit }, { username, password }) {
      console.log('Login action called with:', { username, password });
      try {
        const res = await loginAPI({ username, password }); // 调用登录 API
        console.log('API Response in Vuex:', res);
        // 检查 API 返回的状态是否成功
        if (res && res.message && res.user && typeof res === 'object') {
          commit('login', res.user); // 提交用户信息
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
});

export default store;