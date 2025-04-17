<template>
  <div class="page-header">
    <h2 class="title">订单记录</h2>
    <div class="breadcrumb">当前位置：Home > Orders</div>
  </div>

  <v-container class="py-8 px-6" fluid="">
    <!-- 标题 -->

    <!-- 订单卡片 -->
    <v-row>
      <v-col :key="index" cols="12" lg="4" md="6" v-for="(order, index) in orders">
        <v-card class="custom-card">
          <v-list-item>
            <!-- 订单信息 -->
            <v-list-item-title class="d-flex justify-space-between align-center">
              <span class="order-title">订单 {{ index + 1 }}</span>
              <span class="order-time">{{ order.time }}</span>
            </v-list-item-title>
            <!-- 分割线 -->
            <v-divider class="my-2 custom-divider"></v-divider>
            <!-- 订单详情 -->
            <v-list-item-text class="details">
              <div class="detail-item">
                <strong>商品名称：</strong>{{ order.product }}（{{ order.duration }}）
              </div>
              <div class="detail-item">
                <strong>车辆编号：</strong>{{ order.vehicleId }}
              </div>
              <div class="detail-item">
                <strong>套餐金额：</strong>¥ {{ order.price.toFixed(2) }}
              </div>
              <div class="detail-item">
                <strong>支付方式：</strong>{{ order.paymentMethod }}
              </div>
            </v-list-item-text>
            <!-- 取消订单按钮 -->
            <div class="d-flex justify-end w-100">
              <v-btn @click="cancelOrder(index)" color="error">取消订单</v-btn>
            </div>
          </v-list-item>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
export default {
  data() {
    return {
      // 示例订单数据
      orders: [
        {
          product: '轻型潮流款',
          duration: '1小时',
          vehicleId: 'V001',
          price: 5.00,
          paymentMethod: '余额',
          time: '2025-03-22 17:32'
        },
        {
          product: '性能升级款',
          duration: '4小时',
          vehicleId: 'V002',
          price: 20.00,
          paymentMethod: '信用卡',
          time: '2025-03-22 17:35'
        },
        {
          product: '顶级旗舰款',
          duration: '1天',
          vehicleId: 'V003',
          price: 90.00,
          paymentMethod: '支付宝',
          time: '2025-03-23 00:24'
        }
      ]
    };
  },
  methods: {
    // 取消订单
    cancelOrder(index) {
      if (confirm('确定要取消此订单吗？')) {
        this.orders.splice(index, 1); // 从 orders 数组中移除该订单
        alert('订单已取消');
      }
    }
  }
};

</script>

<style scoped>
.orders-main {
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

.order-title {
  font-weight: bold;
  /* 加粗 */
  color: #757575;
  /* 灰色 */
  font-size: 24px;
  /* 标题字号 */
}

.order-time {
  font-size: 0.9em;
  /* 较小字体 */
  color: #757575;
  /* 灰色 */
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

.page-header {
  margin-top: 24px;
  padding: 24px;
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
</style>