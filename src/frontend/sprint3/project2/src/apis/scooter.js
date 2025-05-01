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