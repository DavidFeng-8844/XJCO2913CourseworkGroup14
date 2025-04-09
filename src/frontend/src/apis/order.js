import http from "@/utils/http";

/**
 * @description: 获取空闲车辆列表
 * @param {*}
 * @return {*}
 */
export const getAvailableVehiclesAPI = () => {
  return http.get('/api/availableVehicles');
};