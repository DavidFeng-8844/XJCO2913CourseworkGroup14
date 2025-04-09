<template>
  <v-main class="store-main">
    <v-container class="py-8 px-6" fluid>
      <!-- 标题 -->
      <v-row>
        <v-col cols="12">
          <h1 class="text-h4 text-left">套餐商店</h1>
        </v-col>
      </v-row>

      <!-- 外层循环：生成三个占据屏幕宽度 100% 的大卡片 -->
      <v-row v-for="(card, index) in cards" :key="card" class="mb-6">
        <v-col cols="12">
          <v-card>
            <v-list lines="two" style="max-height: none; overflow: visible;">
              <!-- 大卡片标题 -->
              <v-list-subheader class="text-h5" :title="card"></v-list-subheader>

              <!-- 内层循环：每个大卡片内生成五个小卡片 -->
              <v-row>
                <v-col v-for="n in 5" :key="n" style="flex: 0 0 30%; max-width: 31%; margin:0 1.5% ;">
                  <v-card class="custom-card">
                    <v-list-item>
                      <!-- 第一排：价格和时间 -->
                      <v-list-item-title class="d-flex justify-space-between align-center">
                        <span class="price">
                          <span class="currency">¥</span>
                          {{ prices[index][n - 1].toFixed(2) }}
                        </span>
                        <span class="time">{{ times[n - 1] }}</span>
                      </v-list-item-title>

                      <!-- 第二排：浅蓝色圆角分割线 -->
                      <v-divider class="my-2 custom-divider"></v-divider>

                      <!-- 第三排：详情 -->
                      <v-list-item-text class="details">
                        <div v-for="(detail, i) in details[index]" :key="i" class="detail-item">
                          <v-icon class="mr-2">{{ icons[index][i] }}</v-icon>
                          {{ detail }}
                        </div>
                      </v-list-item-text>

                      <!-- 立即下单按钮 -->
                      <div class="d-flex justify-center w-100">
                        <v-btn color="primary" class="mt-4" @click="openModal(index, n)">
                          立即下单
                        </v-btn>
                      </div>
                    </v-list-item>
                  </v-card>
                </v-col>
              </v-row>
            </v-list>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- 模态框 -->
    <v-dialog v-model="modalVisible" max-width="500">
      <v-card>
        <v-card-title>确认下单</v-card-title>
        <v-card-text>
          <!-- 商品名称：类型 + 天数 -->
          <div class="modal-text">
            <strong>商品名称：</strong>{{ selectedCard }}（{{ selectedTime }}）
          </div>

          <!-- 优惠额度 -->
          <div class="modal-text">
            <strong>优惠额度：</strong>¥ {{ discount.toFixed(2) }}
          </div>

          <!-- 套餐金额 -->
          <div class="modal-text">
            <strong>套餐金额：</strong>¥ {{ selectedPrice.toFixed(2) }}
          </div>

          <!-- 选择车辆编号 -->
          <v-select v-model="selectedVehicle" :items="availableVehicles" item-text="name" item-value="id" label="选择车辆编号"
            outlined dense></v-select>

          <!-- 支付方式 -->
          <v-radio-group v-model="paymentMethod" label="支付方式">
            <v-radio value="balance" label="余额"></v-radio>
            <v-radio value="creditCard" label="信用卡"></v-radio>
            <v-radio value="alipay" label="支付宝"></v-radio>
          </v-radio-group>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="confirmOrder">确认</v-btn>
          <v-btn color="error" @click="modalVisible = false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-main>
</template>

<script>
export default {
  data: () => ({
    cards: ['轻型潮流款', '性能升级款', '顶级旗舰款'], // 三个大卡片
    prices: [
      [5, 10, 30, 150, 500], // 轻型潮流款的价格
      [10, 20, 60, 300, 1000], // 性能升级款的价格
      [15, 30, 90, 450, 1500], // 顶级旗舰款的价格
    ],
    times: ['1小时', '4小时', '1天', '1周', '1个月'], // 时间
    details: [
      [
        '续航时间：10~15km',
        '重量：1.5kg',
        '三段式可调车头',
        '前后减震',
      ], // 轻型潮流款的详情
      [
        '续航时间：30km',
        '全车IPX4防水',
        '定速巡航',
        '前后方向灯',
        '踏板加宽',
      ], // 性能升级款的详情
      [
        '续航时间：50km',
        '全车IPX5防水',
        'NFC开卡',
        '配备后视镜,手机支架',
      ], // 顶级旗舰款的详情
    ],
    icons: [
      [
        'mdi-clock-outline', // 续航时间
        'mdi-feather', // 重量（羽毛图标）
        'mdi-steering', // 三段式可调车头
        'mdi-car', // 前后减震（替换为汽车图标）
      ],
      [
        'mdi-clock-outline', // 续航时间
        'mdi-water', // 全车IPX4防水
        'mdi-speedometer', // 定速巡航
        'mdi-car-light-high', // 前后方向灯
        'mdi-car-door', // 踏板加宽
      ],
      [
        'mdi-clock-outline', // 续航时间
        'mdi-water', // 全车IPX5防水
        'mdi-nfc', // NFC开卡
        'mdi-mirror', // 配备后视镜,手机支架
      ],
    ],
    modalVisible: false, // 控制模态框显示
    selectedCard: '', // 选中的套餐类型
    selectedTime: '', // 选中的时间
    selectedPrice: 0, // 选中的套餐金额
    discount: 0, // 优惠额度
    paymentMethod: 'balance', // 支付方式（默认余额）
    availableVehicles: [], // 空闲车辆列表
    selectedVehicle: null, // 选中的车辆编号
  }),
  methods: {
    // 打开模态框
    async openModal(cardIndex, timeIndex) {
      this.selectedCard = this.cards[cardIndex];
      this.selectedTime = this.times[timeIndex - 1];
      this.selectedPrice = this.prices[cardIndex][timeIndex - 1];
      this.modalVisible = true;

      // 获取空闲车辆列表
      await this.fetchAvailableVehicles();
    },

    // 获取空闲车辆列表
    async fetchAvailableVehicles() {
      try {
        const response = await fetch('http://localhost:3001/api/availableVehicles');
        const data = await response.json();
        this.availableVehicles = data.map(vehicle => ({
          id: vehicle.id,
          name: `车辆${vehicle.id.toString().padStart(3, '0')}` // 确保名称格式为 "车辆001"
        }));
      } catch (error) {
        console.error('获取空闲车辆列表失败', error);
      }
    },

    // 确认下单
    confirmOrder() {
  const orderDetails = {
    product: this.selectedCard,
    time: this.selectedTime,
    price: this.selectedPrice,
    discount: this.discount,
    paymentMethod: this.paymentMethod,
    vehicleId: this.selectedVehicle, // 这里已经是车辆的 id
  };
  alert(`下单成功：${JSON.stringify(orderDetails, null, 2)}`);
  this.modalVisible = false;
},
  },
}
</script>


<style scoped>
.store-main {
  --v-layout-left: 0 !important;
  /* 取消左侧间距 */
  height: auto;
  /* 确保高度自适应 */
  overflow: visible;
  /* 确保内容不会溢出 */
}

.v-container {
  overflow: visible;
  /* 确保容器不会出现滚动条 */
}

.custom-card {
  border: 2px solid #2196f3;
  /* 蓝色边框 */
  border-radius: 8px;
  /* 圆角 */
  padding: 16px;
  /* 添加内边距 */
  margin: 1rem 0;
}

.price {
  font-weight: bold;
  /* 加粗 */
  color: #757575;
  /* 灰色 */
  font-size: 40px;
  /* 价格字号 */
  display: flex;
  align-items: flex-start;
  /* 价格和符号顶部对齐 */
}

.currency {
  font-size: 24px;
  /* ¥ 符号字号 */
  margin-right: 4px;
  /* 符号与数字的间距 */
  align-self: flex-start;
  /* 符号位于数字左上方 */
}

.time {
  font-size: 0.9em;
  /* 较小字体 */
  color: #757575;
  /* 灰色 */
  align-self: flex-end;
  /* 时间位于数字右下方 */
  margin-left: 8px;
  /* 时间与价格的间距 */
}

.details {
  color: #757575;
  /* 灰色 */
  font-size: 18px;
  /* 详情字号加大 */
  margin-top: 12px;
  /* 与分割线的间距 */
  text-align: left;
  /* 详情内容左对齐 */
  line-height: 1.6;
  /* 加大文字间距 */
}

.detail-item {
  display: flex;
  align-items: center;
  /* 图标和文字垂直居中 */
  margin-bottom: 8px;
  /* 每个详情条目之间的间距 */
}

.custom-divider {
  height: 3px !important;
  /* 加大分割线粗细 */
  background-color: #90caf9;
  /* 浅蓝色 */
  border-radius: 2px;
  /* 圆角 */
}

.v-list-item {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.modal-text {
  margin-bottom: 12px;
  /* 每个文本之间的间距 */
  font-size: 16px;
  /* 文本字号 */
}
</style>