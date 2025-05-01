import {scooter} from "@/utils/http";


/**
* @description: 添加滑板车
* @param {Object} scooterData 滑板车对象
* @return {*}
*/
export function addScooterAPI(scooterData) {
    console.log('Scooter data:', scooterData); // 打印滑板车数据
    return scooter.post('/add-scooter', scooterData);
}


/**
 * @description: 获取最近的滑板车
 * @param {Number} lat 用户纬度
 * @param {Number} lng 用户经度
 * @param {Number} radius 搜索半径（单位：米）
 * @return {Promise} 返回最近的滑板车列表
 */
export function getNearestScootersAPI(lat, lng, radius) {
    return scooter.get('/get-scooters', {
        params: { lat, lng, radius }
    });
}