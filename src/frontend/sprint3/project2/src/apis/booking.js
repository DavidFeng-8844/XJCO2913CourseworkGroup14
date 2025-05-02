import { scooter } from "@/utils/http";

/**
 * @description: 预订滑板车
 * @param {String} scooterId 滑板车ID
 * @param {String} duration 预订时长
 * @return {Promise} 返回预订结果
 */
export function Booking(Booking, scooterId, duration) {
    return scooter.post('/bookings', {
        scooterId,
        duration,
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
    return scooter.delete('/cancel-booking', {
        params: { bookingId },
    });
}