<script setup>
import { ref } from 'vue';
import { loginAPI } from '@/apis/user';
import { registerAPI } from '@/apis/user';
import router from '@/router';
import { ElMessage } from 'element-plus';
import 'element-plus/theme-chalk/el-message.css';

// 登录表单数据
const form = ref({
  account: '',
  password: '',
  agree: true
});

// 注册表单数据
const registerForm = ref({
  email: '',
  username: '',
  password: '',
  confirmPassword: '',
  agree: false
});

// 当前显示的表单（login 或 register）
const activeForm = ref('login');

// 登录表单校验规则
const rules = {
  account: [
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

// 注册表单校验规则
const registerRules = {
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ],
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

// 切换表单
const toggleForm = (formType) => {
  activeForm.value = formType;
};

// 登录逻辑
const formRef = ref(null);
const doLogin = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      const { account, password } = form.value;
      const res = await loginAPI({ account, password });
      ElMessage({ type: 'success', message: '登录成功' });
      router.replace({ path: '/' });
    }
  });
};

// 注册逻辑
const registerFormRef = ref(null);
const doRegister = async () => {
  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      const { email, username, password } = registerForm.value;
      // 调用注册接口
      const res = await registerAPI({ email, username, password });
      ElMessage({ type: 'success', message: '注册成功' });
      // 注册成功后跳转到登录页面
      activeForm.value = 'login';
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
            <!-- 登录表单保持不变 -->
            <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="60px" status-icon>
              <el-form-item label="账户" prop="account">
                <el-input v-model="form.account" />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" show-password />
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
            <!-- 注册表单 -->
            <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-position="right" label-width="80px" status-icon>
              <el-form-item label="邮箱" prop="email" :label-style="{ fontSize: '20px', color: '#fff' }">
                <el-input v-model="registerForm.email" />
              </el-form-item>
              <el-form-item label="用户名" prop="username">
                <el-input v-model="registerForm.username" />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="registerForm.password" show-password />
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="registerForm.confirmPassword" show-password />
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