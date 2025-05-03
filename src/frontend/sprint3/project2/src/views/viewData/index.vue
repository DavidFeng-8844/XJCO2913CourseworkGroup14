<template>
  <div class="data-container">
    <LayoutHeader :is-transparent="false" />
    
    <h1 class="title">数据分析页面</h1>

    <div class="section income-section">
      <h2>每周租赁选项收入</h2>
      <div v-if="weeklyIncome.length">
        <h3>每种租赁选项的每周收入</h3>
        <ul>
          <li v-for="(income, option) in weeklyIncome" :key="option">
            {{ option }}: ${{ income }}
          </li>
        </ul>
      </div>
      <button @click="plotWeeklyIncomeGraph">绘制每周收入图表</button>
      <div v-if="graphData.length">
        <!-- 图表区域 -->
        <canvas id="incomeChart" ref="incomeChart"></canvas>
      </div>
    </div>

    <div class="section daily-income-section">
      <h2>过去一周每日收入</h2>
      <div>
        <ul>
          <li v-for="(income, index) in dailyIncome" :key="index">
            第{{ index + 1 }}天: ${{ income }}
          </li>
        </ul>
        <h3>累计收入: ${{ totalDailyIncome }}</h3>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import LayoutHeader from '@/views/Layout/components/LayoutHeader.vue';
import { Chart, registerables } from 'chart.js';
import { getWeeklyIncomeAPI, getDailyIncomeAPI } from "@/apis/booking"; // 导入相应的 API

// 注册 Chart.js 的所有组件
Chart.register(...registerables);

const store = useStore();
const weeklyIncome = ref({});
const dailyIncome = ref([]);
const totalDailyIncome = ref(0);
const graphData = ref([]);

// 获取每种租赁选项的每周收入
const fetchWeeklyIncome = async () => {
  try {
    const response = await getWeeklyIncomeAPI();
    console.log('每周收入数据:', response.data); // 打印获取到的每周收入数据
    weeklyIncome.value = response.data; // 假设返回的数据形如 { "1小时": 100, "4小时": 300, ... }
  } catch (error) {
    console.error('获取每周收入失败:', error);
  }
};

// 获取过去一周的每日收入
const fetchDailyIncome = async () => {
  try {
    const response = await getDailyIncomeAPI();
    dailyIncome.value = response.data; // 假设返回的数据是一个数组 [100, 200, 150, ...]
    totalDailyIncome.value = dailyIncome.value.reduce((a, b) => a + b, 0); // 计算累计收入
  } catch (error) {
    console.error('获取每日收入失败:', error);
  }
};

// 绘制每周收入图表
const plotWeeklyIncomeGraph = () => {
  const ctx = document.getElementById('incomeChart').getContext('2d');
  
  if (window.incomeChart) {
    window.incomeChart.destroy(); // 销毁旧图表实例
  }
  
  window.incomeChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: Object.keys(weeklyIncome.value), // 租赁选项的标签
      datasets: [
        {
          label: '每周收入',
          data: Object.values(weeklyIncome.value), // 租赁选项对应的收入
          backgroundColor: 'rgba(75, 192, 192, 0.5)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1,
        },
      ],
    },
    options: {
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });
};

// 在页面挂载时初始化数据
onMounted(async () => {
  await fetchWeeklyIncome();
  await fetchDailyIncome();
});
</script>

<style scoped>
.data-container {
  padding: 20px;
}
.income-section, .daily-income-section {
  margin-bottom: 20px;
}
</style>