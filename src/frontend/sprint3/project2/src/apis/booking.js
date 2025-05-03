import { scooter } from "@/utils/http";

/**
 * @description: 预订滑板车
 * @param {String} scooterId 滑板车ID
 * @param {String} duration 预订时长
 * @return {Promise} 返回预订结果
 */
export function Booking(userId, scooterId, durationInHours, cardNumber) {
    // console.log('Booking data:', { userId, scooterId, durationInHours, cardNumber }); // 打印预订数据
    return scooter.get('/bookings', {
        params: {
            userId,
            scooterId,
            durationInHours,
            cardNumber
        }
    });
}

/**
 * @description: 获取用户的预订列表
 * @param {String} userId 用户ID
 * @return {Promise} 返回用户的预订列表
 */
export function getUserBookingsAPI(userId) {
    return scooter.get('/get-bookings', {
        params: { userId },
    });
}

/**
 * @description: 取消预订
 * @param {String} bookingId 预订ID
 * @return {Promise} 返回取消结果
 */
export function cancelBookingAPI(bookingId) {
    return scooter.delete(`/bookings/${bookingId}`); // 使用路径参数
}