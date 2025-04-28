import http from "@/utils/http";

/**
 * @description: 根据user对象进行登录
 * @param {Number} id 分类id
 * @return {*}
 */
// export function loginAPI(user){
//     return http.post('/login',user);
// }
export function loginAPI(user) {
    return http.post('/login', user).then(response => {
        console.log('HTTP Response:', response); 
        // 返回 response.data 确保在后续的调用中可以直接使用它
        return response; 
    }).catch(error => {
        // 可以在这里处理错误，例如记录错误信息
        console.error("Login error:", error);
        throw error; // 重新抛出错误以便后续处理    
    });
}

/**
 * @description: 根据user对象进行注册
 * @param {Object} user 用户对象，包含邮箱、用户名和密码
 * @return {*}
 */

export function registerAPI(user) {
    return http.post('/register', user);
  }