<script setup>
import { ref } from 'vue';
import { loginAPI } from '@/apis/user'; 
import { registerAPI } from '@/apis/user';
import router from '@/router';
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';

const store = useStore(); 

const form = ref({
  username: '',
  password: '',
  agree: true
});

const registerForm = ref({
  email: '',
  username: '',
  password: '',
  confirmPassword: '',
  adminKey: '',
  agree: true
});

const activeForm = ref('login');

const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, max: 14, message: '密码长度要求6-14个字符', trigger: 'blur' }
  ],
  agree: [
    {
      validator(rule, value, callback) {
        if (value) {
          callback();
        } else {
          callback(new Error('请勾选协议'));
        }
      }
    }
  ]
};

const registerRules = {
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, max: 14, message: '密码长度要求6-14个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator(rule, value, callback) {
        if (value === registerForm.value.password) {
          callback();
        } else {
          callback(new Error('两次输入的密码不一致'));
        }
      }
    }
  ],
  agree: [
    {
      validator(rule, value, callback) {
        if (value) {
          callback();
        } else {
          callback(new Error('请勾选协议'));
        }
      }
    }
  ]
};

const toggleForm = (formType) => {
  activeForm.value = formType;  
};

const formRef = ref(null);
const doLogin = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      const { username, password } = form.value;
      try {
        const res = await store.dispatch('login', { username, password });
        console.log('Login Response:', res); // 打印响应数据，用于确认结构
        if (res && res.message) {
                    ElMessage({ type: 'success', message: res.message });
                    router.replace({ path: '/' });
                    
                    // 提交到 Vuex store，确保 userData 结构正确
                    // store.commit('login', { username: res.username, email: res.email });
                    // 不需要在这里提交到 Vuex，因为已经在 login action 中处理了
                }
      } catch (error) {
        console.error("Error in doLogin:", error); 
        ElMessage({ type: 'error', message: error.response?.data || '登录失败' });
      }
    }
  });
};

const registerFormRef = ref(null);
const doRegister = async () => {
  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      const { email, username, password, adminKey } = registerForm.value;
      try {
        const res = await registerAPI({email, username, password}, adminKey);
        ElMessage({ type: 'success', message: res.data || 'Register Success' }); // 使用后端返回的成功消息
        activeForm.value = 'login'; // 注册成功后切换到登录
      } catch (error) {
        ElMessage({ type: 'error', message: error.response?.data || '注册失败' });
      }
    }
  });
};
</script>

<template>
  <div class="login-container">
    <section class="login-section">
      <div class="wrapper">
        <nav>
          <a href="javascript:;" @click="toggleForm('login')" :class="{ active: activeForm === 'login' }">账户登录</a>
          <a href="javascript:;" @click="toggleForm('register')" :class="{ active: activeForm === 'register' }">注册</a>
        </nav>
        <div class="account-box">
          <div class="form" v-if="activeForm === 'login'">
            <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="60px" status-icon>
              <el-form-item label="账户" prop="username">
                <el-input v-model="form.username" />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" type="password" />
              </el-form-item>
              <el-form-item label-width="22px" prop="agree">
                <el-checkbox v-model="form.agree" size="large">
                  我已同意隐私条款和服务条款
                </el-checkbox>
              </el-form-item>
              <el-button size="large" class="subBtn" @click="doLogin">点击登录</el-button>
            </el-form>
          </div>
          <div class="form" v-if="activeForm === 'register'">
            <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-position="right" label-width="80px" status-icon>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="registerForm.email" />
              </el-form-item>
              <el-form-item label="用户名" prop="username">
                <el-input v-model="registerForm.username" />
              </el-form-item>
              <el-form-item label="管理员密钥" prop="adminKey">
                <el-input v-model="registerForm.adminKey" placeholder="留空则为普通用户" />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="registerForm.password" type="password" />
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="registerForm.confirmPassword" type="password" />
              </el-form-item>
              <el-form-item label-width="22px" prop="agree">
                <el-checkbox v-model="registerForm.agree" size="large">
                  我已同意隐私条款和服务条款
                </el-checkbox>
              </el-form-item>
              <el-button size="large" class="subBtn" @click="doRegister">注册</el-button>
            </el-form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped lang="scss">
/* 您的样式代码保持不变 */
</style>

<style scoped lang="scss">
.login-container {
  height: 100vh; /* 使容器占据整个屏幕高度 */
  background: url('@/assets/images/login5.png') no-repeat center / cover; /* 背景图 */
  position: relative; /* 相对定位 */
}

.login-section {
  position: absolute;
  top: 50%;
  right: 10%; /* 调整到屏幕右侧 */
  transform: translateY(-50%); /* 垂直居中 */
  width: 450px; /* 调大注册框宽度 */
  background: #1e1e1e; /* 主色调 */
  border-radius: 20px; /* 圆角 */
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5); /* 阴影 */
  padding: 30px; /* 增加内边距 */
}

nav {
  font-size: 14px;
  height: 55px;
  margin-bottom: 20px;
  border-bottom: 1px solid #fff; /* 分割线改为白色 */
  display: flex;
  padding: 0 20px;
  text-align: right;
  align-items: center;

  a {
    flex: 1;
    line-height: 1;
    display: inline-block;
    font-size: 18px;
    position: relative;
    text-align: center;
    color: #fff; /* 文字颜色 */
    text-decoration: none;

    &.active {
      color: #00dd00; /* 主色调 */
      font-weight: bold;
    }
  }
}

.account-box {
  .form {
    padding: 0 20px 20px 20px;

    &-item {
      margin-bottom: 28px;

      :deep(.el-form-item__label) {
        font-size: 20px; /* 标签字体大小 */
        color: #fff; /* 标签文字颜色 */
      }

      .el-input {
        background: #333; /* 输入框背景色 */
        border-radius: 8px; /* 输入框圆角 */
        border: 1px solid #555; /* 输入框边框 */

        input {
          color: #fff; /* 输入文字颜色 */
          background: transparent; /* 输入框背景透明 */
        }
      }

      .el-checkbox {
        color: #fff; /* 复选框文字颜色 */
      }
    }
  }
}

.subBtn {
  background: #00dd00; /* 主色调 */
  width: 100%;
  color: #fff;
  border-radius: 8px; /* 按钮圆角 */
  border: none; /* 去掉边框 */
  font-weight: bold; /* 加粗文字 */
  transition: background 0.3s ease; /* 按钮 hover 动画 */

  &:hover {
    background: #00bb00; /* 按钮 hover 颜色 */
  }
}
</style>