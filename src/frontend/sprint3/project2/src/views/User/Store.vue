<template>
  <div class="store-page">
    <div class="page-header">
      <h2 class="title">套餐商店</h2>
      <div class="breadcrumb">当前位置：Home > Store</div>
    </div>

    <v-card class="tab-wrapper">
      <v-tabs v-model="activeTab" background-color="white" grow>
        <v-tab v-for="(card, i) in cards" :key="i">
          <v-icon left>mdi-scooter</v-icon>{{ card }}
        </v-tab>
      </v-tabs>
      <v-divider></v-divider>
      <div class="tab-content">
        <div class="card-list">
          <div class="card" v-for="(price, index) in prices[activeTab]" :key="index" @mouseover="handleMouseOver(index)"
            @mouseleave="handleMouseLeave(index)">
            <div class="card-image">
              <img :src="getImage(index)" alt="Scooter Image" :class="{ 'hovered': isHovered[index] }" />
              <button v-if="isHovered[index]" class="order-btn"
                @click="openModal(cards[activeTab], times[index], price)">
                立即下单
              </button>
            </div>
            <div class="card-description">
              <p>
                {{ getRentalPlan(times[index]) }} {{ cards[activeTab] }} | {{ times[index] }} <br>
                <strong><span class="price-amount">¥ {{ price }}</span></strong>
              </p>
            </div>

          </div>
        </div>
      </div>
    </v-card>

    <!-- 模态框 -->
    <v-dialog max-width="500" v-model="modalVisible">
      <v-card>
        <v-card-title>确认下单</v-card-title>
        <v-card-text>
          <div class="modal-text">
            <strong>商品名称：</strong>{{ selectedCard }}（{{ selectedTime }}）
          </div>
          <div class="modal-text">
            <strong>优惠额度：</strong>¥ {{ discount.toFixed(2) }}
          </div>
          <div class="modal-text">
            <strong>套餐金额：</strong>¥ {{ selectedPrice.toFixed(2) }}
          </div>
          <v-select :items="availableVehicles" dense item-text="name" item-value="id" label="选择车辆编号" outlined
            v-model="selectedVehicle"></v-select>
          <v-radio-group label="支付方式" v-model="paymentMethod">
            <v-radio label="余额" value="balance"></v-radio>
            <v-radio label="信用卡" value="creditCard"></v-radio>
            <v-radio label="支付宝" value="alipay"></v-radio>
          </v-radio-group>
        </v-card-text>
        <v-card-actions>
          <v-btn @click="confirmOrder" color="primary">确认</v-btn>
          <v-btn @click="modalVisible = false" color="error">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SwiftOne from '@/assets/images/store/SwiftOne.png'
import SwiftOneHover from '@/assets/images/store/SwiftOneHover.png'
import SwiftOneHoverLong from '@/assets/images/store/SwiftOneHoverLong.png'
import SwiftX from '@/assets/images/store/SwiftX.png'
import VoltRunnerX from '@/assets/images/store/VoltRunnerX.png'
import SwiftXHover from '@/assets/images/store/SwiftXHover.png'
import VoltRunnerXHover from '@/assets/images/store/VoltRunnerXHover.png'

const activeTab = ref(0)
const cards = ['SwiftOne', 'SwiftX', 'VoltRunner X']
const prices = [
  [5, 10, 30, 150, 500],
  [10, 20, 60, 300, 1000],
  [15, 30, 90, 450, 1500]
]
const times = ['1小时', '4小时', '1天', '1周', '1个月']


const modalVisible = ref(false)
const selectedCard = ref('')
const selectedTime = ref('')
const selectedPrice = ref(0)
const discount = ref(0)
const paymentMethod = ref('balance')
const availableVehicles = ref([
  { id: 1, name: '车辆 #001' },
  { id: 2, name: '车辆 #002' },
  { id: 3, name: '车辆 #003' },
  { id: 4, name: '车辆 #004' }
])
const selectedVehicle = ref(null)

// Hover 状态数组，用来控制每个卡片的 hover 状态
const isHovered = ref([])

// 处理 hover 事件
function handleMouseOver(index) {
  isHovered.value[index] = true
}

function handleMouseLeave(index) {
  isHovered.value[index] = false
}

// 图片切换逻辑
// 图片切换逻辑
function getImage(index) {
  const currentCard = cards[activeTab.value]
  const isHover = isHovered.value[index]
  const currentTime = times[index] // Get the time (1小时, 4小时, etc.)

  // 根据当前标签页和租赁计划返回对应图片
  switch (currentCard) {
    case 'SwiftOne':
      // If short-term, use SwiftOneHover, otherwise use SwiftOneHoverLong
      return isHover 
        ? (currentTime === '1小时' || currentTime === '4小时' ? SwiftOneHover : SwiftOneHoverLong) 
        : SwiftOne
    case 'SwiftX':
      return isHover ? SwiftXHover : SwiftX
    case 'VoltRunner X':
      return isHover ? VoltRunnerXHover : VoltRunnerX
    default:
      return SwiftOne
  }
}


// 获取租赁计划
function getRentalPlan(time) {
  return time === '1小时' || time === '4小时' ? '[ SHORT-TERM RENTAL ]' : '[ LONG-TERM RENTAL ]'
}

function openModal(card, time, price) {
  selectedCard.value = card
  selectedTime.value = time
  selectedPrice.value = price
  discount.value = price * 0.1
  modalVisible.value = true
}

function confirmOrder() {
  modalVisible.value = false
  alert(`下单成功：¥${selectedPrice.value}，使用${selectedTime.value}，方式：${paymentMethod.value}`)
}
</script>

<style scoped>
.store-page {
  padding: 24px;
  font-family: 'Segoe UI', sans-serif;
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
  margin-bottom: 20px;
}

.tab-wrapper {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  background: white;
  padding: 16px;
}

.card-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  padding-top: 16px;
}

.card {
  background: white;
  border: none;
  /* 取消边框 */
  padding: 0;
  /* 取消内边距 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-image {
  position: relative;
  overflow: hidden;
  padding: 0;
  /* 取消图片的内边距 */
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  margin: 0;
  /* 取消图片的外边距 */
}

.card-image .order-btn {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  color: black;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  width: 80%;
  border: 1px solid black; /* Add border with 2px width and black color */
}


.card-description {
  font-size: 14px;
  color: #444;
  text-align: center;
  margin-top: 10px;
}

.card-price {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  font-size: 22px;
  margin-bottom: 12px;
}


.duration {
  font-size: 14px;
  font-weight: normal;
  color: #555;
}

.features {
  list-style: none;
  padding-left: 0;
  font-size: 14px;
  color: #444;
  margin-bottom: 16px;
}

.features li {
  margin: 6px 0;
  display: flex;
  align-items: center;
  gap: 6px;
}

.modal-text {
  margin-bottom: 12px;
  font-size: 16px;
}

@media (max-width: 600px) {
  .card-list {
    grid-template-columns: 1fr !important;
  }

  .card {
    width: 100%;
  }
}

.card-image img {
  transition: all 0.3s ease;
}

.card-image img.hovered {
  transform: scale(1.05);
  filter: brightness(0.7);
}
</style>
