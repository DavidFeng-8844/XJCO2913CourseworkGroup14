<template>
  <div class="data-container">
    <LayoutHeader :is-transparent="false" />

    <h1 class="title">Data Dashboard</h1>

    <div class="section income-section">
      <h2>Weekly Income by Rental Option</h2>
      <table>
        <thead>
          <tr>
            <th>Rental Option</th>
            <th>Weekly Income</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(income, option) in weeklyIncome" :key="option">
            <td>{{ option }}</td>
            <td>${{ income.toFixed(2) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="section daily-income-section">
      <h2>Daily Income Over the Past Week</h2>
      <table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Daily Income</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(income, date) in dailyIncome" :key="date">
            <td>{{ date }}</td>
            <td>${{ income.toFixed(2) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="section graph-section">
      <h2>Weekly Income Graph</h2>
      <canvas id="weeklyIncomeGraph"></canvas>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import LayoutHeader from '@/views/Layout/components/LayoutHeader.vue';
import { getWeeklyIncomeAPI, getDailyIncomeAPI } from '@/apis/booking'; // 假设你有相应的 API
import Chart from 'chart.js/auto';

const weeklyIncome = ref({});
const dailyIncome = ref({});

const fetchIncomeData = async () => {
  try {
    weeklyIncome.value = (await getWeeklyIncomeAPI()); // 从 API 获取每周收入
    console.log('Weekly Income:', weeklyIncome.value);
    dailyIncome.value = (await getDailyIncomeAPI()); // 从 API 获取每日收入
    plotWeeklyIncomeGraph(); // 绘制图表
  } catch (error) {
    console.error('获取收入数据失败:', error);
  }
};

const plotWeeklyIncomeGraph = () => {
  const ctx = document.getElementById('weeklyIncomeGraph').getContext('2d');
  const labels = Object.keys(weeklyIncome.value);
  const data = Object.values(weeklyIncome.value);

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Weekly Income ($)',
        data: data,
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
};

onMounted(() => {
  fetchIncomeData();
});
</script>

<style scoped>
.data-container {
  padding: 20px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.section {
  margin-bottom: 40px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}
</style>