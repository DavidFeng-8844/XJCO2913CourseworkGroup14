<template>
    <div class="add-scooter-container">
      <LayoutHeader :is-transparent="false" />
      <h1>新增滑板车</h1>
      <form @submit.prevent="addScooter">
        <div class="input-field">
          <label for="scooterId">滑板车ID:</label>
          <input
            type="text"
            id="scooterId"
            v-model="scooterId"
            placeholder="请输入滑板车ID"
            required
          />
        </div>
        <div class="input-field">
          <label for="latitude">纬度:</label>
          <input
            type="number"
            id="latitude"
            v-model="latitude"
            placeholder="请输入纬度"
            step="0.000001"
            required
          />
        </div>
        <div class="input-field">
          <label for="longitude">经度:</label>
          <input
            type="number"
            id="longitude"
            v-model="longitude"
            placeholder="请输入经度"
            step="0.000001"
            required
          />
        </div>
        <button type="submit" class="submit-button">添加滑板车</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue";
  import axios from "axios";
  import LayoutHeader from '@/views/Layout/components/LayoutHeader.vue';
  import { addScooterAPI } from "@/apis/scooter";
  
  const scooterId = ref("");
  const latitude = ref("");
  const longitude = ref("");
  
  const addScooter = async () => {
    if (!scooterId.value || !latitude.value || !longitude.value) {
      alert("请填写所有字段！");
      return;
    }
  
    try {
      // const response = await axios.post("http://localhost:8080/api/scooters/add-scooters", {
      //   id: scooterId.value,
      //   locationLat: parseFloat(latitude.value),
      //   locationLng: parseFloat(longitude.value),
      //   status: "available",
      // });
      const response = await addScooterAPI({
        id: scooterId.value,
        locationLat: parseFloat(latitude.value),
        locationLng: parseFloat(longitude.value),
        status: "available",
      });
      alert("滑板车添加成功！");
      console.log(response.data);
      // 清空输入框
      scooterId.value = "";
      latitude.value = "";
      longitude.value = "";
    } catch (error) {
  console.error("添加滑板车失败:", error.response ? error.response.data : error);
  alert(error.response?.data?.message || "添加滑板车失败，请重试！");
}
  };
  </script>
  
  <style scoped>
  .add-scooter-container {
    max-width: 500px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }
  
  h1 {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .input-field {
    margin-bottom: 15px;
  }
  
  .input-field label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  .input-field input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  .submit-button {
    width: 100%;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .submit-button:hover {
    background-color: #0056b3;
  }
  </style>