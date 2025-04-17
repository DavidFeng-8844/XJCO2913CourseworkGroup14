<template>
  <div class="wallet-page">
    <div class="page-header">
      <div class="title-row">
        <h2 class="title">钱包充值</h2>
        <v-icon class="toggle-button" @click="drawerVisible = true" size="24">
          mdi-dots-vertical
        </v-icon>
      </div>
      <div class="breadcrumb">当前位置：Home > Wallet</div>
    </div>


    <!-- Drawer 抽屉（移动端显示 tab-switch） -->
    <el-drawer v-model="drawerVisible" direction="rtl" size="200px" :with-header="false">
      <div class="tab-switch-drawer">
        <button :class="{ active: activeTab === 'online' }" @click="switchTab('online')">在线充值</button>
        <button :class="{ active: activeTab === 'view' }" @click="switchTab('view')">查看信用卡</button>
        <button :class="{ active: activeTab === 'credit' }" @click="switchTab('credit')">添加信用卡</button>
      </div>
    </el-drawer>


    <!-- 主体区域：左右分布 -->
    <div class="wallet-content">
      <!-- 左侧切换按钮 -->
      <div class="tab-switch">
        <button :class="{ active: activeTab === 'online' }" @click="activeTab = 'online'">在线充值</button>
        <button :class="{ active: activeTab === 'view' }" @click="activeTab = 'view'">查看信用卡</button>
        <button :class="{ active: activeTab === 'credit' }" @click="activeTab = 'credit'">添加信用卡</button>

      </div>




      <!-- 右侧内容展示区域 -->
      <div class="tab-content">
        <!-- 在线充值区域 -->
        <div v-if="activeTab === 'online'" class="online-panel">
          <!-- 钱包充值 -->
          <div class="card recharge-panel">
            <h3>钱包充值</h3>
            <p>账户余额：<strong>￥{{ balance.toFixed(2) }}</strong></p>
            <input v-model="rechargeAmount" type="number" placeholder="请输入充值金额" />

            <div class="btn-group">
              <button class="primary half-width" @click="dialogVisible = true">立即充值</button>
            </div>
          </div>

          <!-- 充值记录 -->
          <div class="card record-panel">
            <div class="record-header">
              <h3>充值记录</h3>
            </div>
            <table class="record-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>类型</th>
                  <th>金额</th>
                  <th>使用时间</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="record in rechargeRecords" :key="record.id">
                  <td>{{ record.id }}</td>
                  <td>{{ record.type }}</td>
                  <td>{{ record.amount }} 元</td>
                  <td>{{ record.time }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 添加信用卡区域 -->
        <div v-if="activeTab === 'credit'" class="credit-panel card">
          <CreditCardForm />
        </div>

        <!-- 查看信用卡 -->
        <div v-if="activeTab === 'view'" class="view-panel card">
          <div class="card-preview-group">
            <CreditCardPreview :cards="savedCards" @delete="deleteCard" />
          </div>
        </div>


      </div>
    </div>
  </div>


  <el-dialog v-model="dialogVisible" title="选择支付方式" width="400px" center>
    <el-radio-group v-model="paymentMethod" class="payment-options">
      <el-radio label="信用卡">信用卡</el-radio>
      <el-radio label="支付宝">支付宝</el-radio>
    </el-radio-group>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmPayment">确认支付</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import CreditCardForm from './components/CreditCardForm.vue'
import CreditCardPreview from './components/CreditCardPreview.vue'
import { ElMessage } from 'element-plus'
const activeTab = ref('online')
const balance = ref(0.0)
const dialogVisible = ref(false)
const rechargeAmount = ref('')
const paymentMethod = ref('信用卡') // 默认选项

const rechargeRecords = ref([
  { id: 88722, type: '金额充值', amount: 184.0, time: '2024-10-04 11:51:56' },
  { id: 86860, type: '金额充值', amount: 18.05, time: '2024-09-03 10:25:50' }
])

function confirmPayment() {
  dialogVisible.value = false
  ElMessage.success(`您已选择 ${paymentMethod.value} 支付 ¥${rechargeAmount.value}`)
  rechargeAmount.value = ''
}


const savedCards = ref([
  {
    number: '1234567812345678',
    name: 'Jack Ma',
    month: '08',
    year: '2027',
    type: 'visa',
    background: '12.jpeg'
  },
  {
    number: '5555444433332222',
    name: 'Elon Musk',
    month: '12',
    year: '2029',
    type: 'mastercard',
    background: '3.jpeg'
  }
])

function deleteCard(index) {
  savedCards.value.splice(index, 1)
}

const drawerVisible = ref(false)

function switchTab(tab) {
  activeTab.value = tab
  drawerVisible.value = false
}

</script>

<style lang="scss">
.wallet-page {
  padding: 24px;
  font-family: 'Segoe UI', sans-serif;
}

.page-header {
  margin-top: 24px;
  margin-bottom: 12px;
}

.title {
  font-size: 24px;
  margin-bottom: 8px;
}

.breadcrumb {
  color: #888;
  margin-bottom: 24px;
  font-size: 14px;
}

/* 主体结构 */
.wallet-content {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

/* 左侧按钮区域 */
.tab-switch {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 150px;
}

.tab-switch button {
  padding: 12px;
  background: #f0f0f0;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.tab-switch button.active {
  background-color: #1976d2;
  color: white;
}

/* 右侧内容区域 */
.tab-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
  min-width: 0;
  overflow-x: hidden;
}


/* 卡片通用 */
.card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

/* 在线充值卡片 */
.recharge-panel input {
  width: 100%;
  margin: 12px 0;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.btn-group {
  display: flex;
  justify-content: flex-end;
}

.btn-group button {
  padding: 10px;
  border: none;
  background: #f1f1f1;
  border-radius: 4px;
  cursor: pointer;
}

.btn-group button.primary {
  background-color: #0066ff;
  color: white;
}

/* 记录表格样式 */
.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-header button.small {
  font-size: 13px;
  padding: 4px 12px;
  background-color: #0066ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.record-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 16px;
}

.record-table th,
.record-table td {
  border-bottom: 1px solid #eee;
  padding: 8px 12px;
  text-align: left;
  font-size: 14px;
}

.credit-panel {
  min-height: 300px;
}


button.half-width {
  width: 50%;
}

.payment-options {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
}



.title-row {
  display: flex;
  align-items: center;
  gap: 8px; 
}


.toggle-button {
  display: none;
}

.tab-switch-drawer {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px;
}

.tab-switch-drawer button {
  padding: 12px;
  background: #f0f0f0;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.tab-switch-drawer button.active {
  background-color: #1976d2;
  color: white;
}


/* 响应式调整：小屏幕时隐藏左侧导航栏 */
@media (max-width: 768px) {
  .tab-switch {
    display: none; // 原本的侧边栏隐藏
  }

  .toggle-button {
    display: inline-flex
  }
}
</style>
