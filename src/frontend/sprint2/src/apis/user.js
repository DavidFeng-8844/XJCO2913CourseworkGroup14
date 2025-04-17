import http from "@/utils/http";

/**
 * @description: 根据user对象进行登录
 * @param {Number} id 分类id
 * @return {*}
 */
export function loginAPI(user){
    return http.post('/login',user);
}


/**
 * @description: 根据user对象进行注册
 * @param {Object} user 用户对象，包含邮箱、用户名和密码
 * @return {*}
 */

export function registerAPI(user) {
    return http.post('/register', user);
  }