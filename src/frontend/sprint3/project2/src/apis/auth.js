// apis/auth.js
import axios from 'axios';

const API_URL = 'https://your-api-url.com'; // 替换为实际的 API 地址

export const loginAPI = async (credentials) => {
  const response = await axios.post(`${API_URL}/login`, credentials);
  return response.data; // 返回响应数据
};