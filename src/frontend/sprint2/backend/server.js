const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());

// 模拟空闲车辆列表
const availableVehicles = [
  { id: 'V001', name: '车辆001' },
  { id: 'V002', name: '车辆002' },
  { id: 'V003', name: '车辆003' },
];

// 获取空闲车辆列表的接口
app.get('/api/availableVehicles', (req, res) => {
  res.json(availableVehicles);
});

// 启动服务器
app.listen(3001, () => {
  console.log('后端服务已启动，访问 http://localhost:3001/api/availableVehicles');
});