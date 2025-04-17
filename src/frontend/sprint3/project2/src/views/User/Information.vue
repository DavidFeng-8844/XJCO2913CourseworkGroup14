<template>
  <div class="information-page">
    <div class="page-header">
      <h2 class="title">我的账号</h2>
      <div class="breadcrumb">当前位置：Home > Account</div>
    </div>

    <v-row class="mt-4" dense>
      <v-col cols="12" md="8">
        <v-row class="gap-y-8">
          <v-col v-for="item in functionCards" :key="item.title" cols="12" sm="6"  @click="handleCardClick(item.action)">
            <v-card :style="{ background: item.bg, height: '160px', cursor: 'pointer' }" dark class="pa-5">
              <div class="d-flex justify-space-between align-center mb-3">
                <span class="text-h6">{{ item.title }}</span>
                <v-icon size="24">{{ item.icon }}</v-icon>
              </div>
              <div class="text-body-2">{{ item.desc }}</div>
            </v-card>
          </v-col>
        </v-row>
      </v-col>


      <v-col cols="12" md="4" class="user-card">
        <v-card class="pa-0 overflow-hidden">
          <!-- 顶部封面图 -->
          <div class="cover-image">
            <img src="@/assets/images/banner.jpg" alt="cover" />
          </div>

          <!-- 头像（覆盖在封面上） -->
          <v-avatar size="96" class="avatar-overlap">
            <img src="@/assets/images/avatar.png" alt="avatar" />
          </v-avatar>

          <v-card-text class="text-center mt-8">
            <v-btn color="primary" class="ma-2" small>我的钱包</v-btn>
            <v-btn color="primary" class="ma-2" small>前往商店</v-btn>


            <div class="text-h6 font-weight-medium mt-2 text-black">5ubmerge</div>
            <div class="text-body-2 text-grey">18884730609@163.com</div>
          </v-card-text>
        </v-card>
      </v-col>

    </v-row>
  </div>

  <!-- 修改密码模态框 -->
  <el-dialog v-model="passwordDialog" title="修改密码" width="500px">
    <el-form label-width="100px">
      <el-form-item label="当前密码:">
        <el-input v-model="oldPassword" placeholder="Old passwd" type="password" />
      </el-form-item>
      <el-form-item label="新密码:">
        <el-input v-model="newPassword" placeholder="New passwd" type="password" />
      </el-form-item>
      <el-form-item label="重复新密码">
        <el-input v-model="confirmPassword" placeholder="RE New passwd" type="password" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="passwordDialog = false">取消</el-button>
      <el-button type="primary" @click="submitPasswordChange">确认提交</el-button>
    </template>
  </el-dialog>

  <!-- 查看优惠券模态框 -->
  <el-dialog v-model="couponDialog" title="我的优惠券" width="600px">
    <el-table :data="coupons" border style="width: 100%">
      <el-table-column prop="discount" label="折扣" width="120" />
      <el-table-column prop="expire" label="到期时间" width="200" />
      <el-table-column prop="quantity" label="数量" />
    </el-table>
    <template #footer>
      <el-button @click="couponDialog = false">关闭</el-button>
    </template>
  </el-dialog>

</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { ElDialog } from 'element-plus'

ElDialog.props.lockScroll.default = false
const functionCards = [
  {
    title: '修改登录密码',
    desc: '定期修改为高强度密码以保护您的账号。',
    icon: 'mdi-lock',
    bg: 'linear-gradient(to right, #d32f2f, #ef5350)',
    action: 'password'
  },
  {
    title: '查看优惠券',
    desc: '管理和使用您已获得的专属优惠券。',
    icon: 'mdi-ticket-percent',
    bg: 'linear-gradient(to right, #f57c00, #ffb74d)',
    action: 'coupons'
  },
  {
    title: '异常登录通知',
    desc: '启用后将在检测到异常登录时提醒您。',
    icon: 'mdi-alert-octagon',
    bg: 'linear-gradient(to right, #0288d1, #26c6da)'
  },
  {
    title: '消息中心',
    desc: '查看系统通知和平台推送的最新消息。',
    icon: 'mdi-bell-ring',
    bg: 'linear-gradient(to right, #388e3c, #66bb6a)'
  },
  {
    title: '联系客服',
    desc: '遇到问题？点击这里与我们取得联系。',
    icon: 'mdi-headset',
    bg: 'linear-gradient(to right, #7b1fa2, #ba68c8)'
  },
  {
    title: '账户注销',
    desc: '提交注销申请后您的账户将被永久删除。',
    icon: 'mdi-account-remove',
    bg: 'linear-gradient(to right, #4a148c, #7e57c2)'
  }
]

// 模态框控制
const passwordDialog = ref(false)
const couponDialog = ref(false)

// 修改密码数据
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')

// 优惠券数据
const coupons = ref([
  { discount: '9折', expire: '2025-06-01', quantity: 2 },
  { discount: '8折', expire: '2025-07-15', quantity: 1 }
])

// 点击卡片触发模态框
function handleCardClick(action) {
  if (action === 'password') {
    passwordDialog.value = true
  } else if (action === 'coupons') {
    couponDialog.value = true
  }
}

function submitPasswordChange() {
  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    ElMessage.error('请填写所有字段')
    return
  }
  if (newPassword.value !== confirmPassword.value) {
    ElMessage.error('两次新密码输入不一致')
    return
  }
  ElMessage.success('密码修改成功（模拟）')
  passwordDialog.value = false
}
</script>


<style scoped>
.information-page {
  padding: 24px;
}

.page-header {
  margin-top: 24px;
  margin-bottom: 12px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.breadcrumb {
  color: #888;
  font-size: 14px;
}

.v-col {
  margin-bottom: 24px;
  /* 增加卡片之间的垂直间距 */
}

.v-card {
  transition: box-shadow 0.2s ease;
  min-height: 140px;
  /* 增加卡片整体高度 */
  color: white
}

.v-card .text-h6 {
  font-size: 20px;
  margin-bottom: 20px;
}


.v-card .text-body-2 {
  font-size: 15px;
  line-height: 1.6;
}

.v-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.cover-image img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}

.avatar-overlap {
  position: absolute;
  top: 60px;
  left: 50%;
  transform: translateX(-50%);
  border: 4px solid white;
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 2;
}

.avatar-overlap:hover {
  transform: translateX(-50%) scale(1.05);
  /* hover 放大 */
}

.avatar-overlap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 保证图片完整显示 */
  border-radius: 50%;
}

.user-card .v-card {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
}
</style>