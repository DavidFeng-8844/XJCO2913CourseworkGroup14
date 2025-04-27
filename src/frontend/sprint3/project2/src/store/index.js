// stores/index.js
import { createStore } from 'vuex';

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
    login({ commit }, user) {
      commit('login', user);
    },
    logout({ commit }) {
      commit('logout');
    },
  },
});

export default store;